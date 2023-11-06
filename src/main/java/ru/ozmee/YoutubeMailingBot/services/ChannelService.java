package ru.ozmee.YoutubeMailingBot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ozmee.YoutubeMailingBot.models.Channel;
import ru.ozmee.YoutubeMailingBot.models.Chat;
import ru.ozmee.YoutubeMailingBot.repositories.ChannelRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ChannelService {
    private final ChannelRepository channelRepository;
    @Autowired
    public ChannelService(ChannelRepository channelRepository){
        this.channelRepository = channelRepository;
    }

    public List<Channel> findAll(){
        return channelRepository.findAll();
    }

    public Channel findById(int id){
        return channelRepository.findById(id);
    }
    @Transactional
    public void save(Channel channel){
        channelRepository.save(channel);
    }

    @Transactional
    public Channel findByVideosHttpOrElseCreate(String videosHttp, String lastVideoHttp){
        Optional<Channel> checkedChannel = channelRepository.findByVideosHttp(videosHttp);
        if(checkedChannel.isEmpty()){
            Channel newChannel = new Channel();
            newChannel.setVideosHttp(videosHttp);
            newChannel.setLastVideoHttp(lastVideoHttp);
            save(newChannel);
            return newChannel;
        }
        return checkedChannel.get();
    }

    @Transactional
    public void deleteChannelIfNotUse(int id){
        Channel channel = findById(id);
        if(channel.getChats().isEmpty()){
            channelRepository.delete(channel);
        }
    }

    @Transactional
    public void delete(Channel channel){
        List<Chat> chats = channel.getChats();
        for(Chat chat : chats){
            chat.getChannels().remove(channel);
        }
        channelRepository.delete(channel);
    }
}
