package ru.ozmee.YoutubeMailingBot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.ozmee.YoutubeMailingBot.IOMessage.MessageHandler;
import ru.ozmee.YoutubeMailingBot.models.Channel;
import ru.ozmee.YoutubeMailingBot.models.Chat;
import ru.ozmee.YoutubeMailingBot.services.ChannelService;
import ru.ozmee.YoutubeMailingBot.util.ConverterHttp;
import ru.ozmee.YoutubeMailingBot.util.NotFoundLastVideoException;

import java.util.List;

@Slf4j
@Component
public class Schedule {
    private final Bot bot;
    private final MessageHandler messageHandler;
    private final ChannelService channelService;
    private final ConverterHttp converterHttp;

    @Autowired
    public Schedule(Bot bot, MessageHandler messageHandler, ChannelService channelService, ConverterHttp converterHttp){
        this.bot = bot;
        this.messageHandler = messageHandler;
        this.channelService = channelService;
        this.converterHttp = converterHttp;
    }

    @Async
    @Scheduled(cron="${cron.scheduler}")
    public void checkLastVideo(){
        List<Channel> list = channelService.findAll();
        for(Channel channel : list){
            String lastVideoHttp;
            try {
                lastVideoHttp = converterHttp.convertVideosHttpToLastVideoHttp(channel.getVideosHttp());
            } catch (NotFoundLastVideoException e) {
                channelService.delete(channel);
                continue;
            }
            if(!channel.getLastVideoHttp().equals(lastVideoHttp)){
                channel.setLastVideoHttp(converterHttp.convertVideosHttpToLastVideoHttp(channel.getVideosHttp()));
                channelService.save(channel);
                notifyAboutNewVideo(channel);
            }
        }
    }

    public void notifyAboutNewVideo(Channel channel){
        String authorName = converterHttp.getAuthorOfChannel(channel);
        List<Chat> chats = channel.getChats();
        for(Chat chat : chats){
            String msg = messageHandler.notifyMessage(authorName, channel.getLastVideoHttp());
            bot.executeMessage(new SendMessage(Long.toString(chat.getChatId()), msg));
        }
    }
}