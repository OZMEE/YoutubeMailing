package ru.ozmee.YoutubeMailingBot.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ozmee.YoutubeMailingBot.models.Channel;
import ru.ozmee.YoutubeMailingBot.models.Chat;
import ru.ozmee.YoutubeMailingBot.repositories.ChatRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Component
public class ChatService {
    private final ChatRepository chatRepository;

    @Value("${chat.maxCountChannels}")
    private int maxCountChannels;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
    @Transactional
    public void ifNotExistCreate(Long chatId){
        Optional<Chat> checkedChat = chatRepository.findByChatId(chatId);
        if(checkedChat.isEmpty()){
            Chat newChat = new Chat();
            newChat.setChatId(chatId);
            save(newChat);
        }
    }

    public boolean isValidCountChannels(Long chatId){
        List<Channel> channels = findByChatId(chatId).getChannels();
        return channels==null || channels.size()< maxCountChannels;
    }

    @Transactional
    public void deleteChannelFromChat(Long chatId, int channelId){
        Chat chat = findByChatId(chatId);
        List<Channel> channels = chat.getChannels();

        Channel channel = channels.stream().filter(channel1 -> channel1.getId()==channelId).toList().get(0);

        channel.getChats().remove(chat);
        channels.remove(channel);
        chatRepository.save(chat);
    }

    public boolean chatHaveChannel(Long chatId, int channelId){
        Chat chat = findByChatId(chatId);
        List<Channel> channels = chat.getChannels();

        return channels.stream().anyMatch(channel -> channel.getId() == channelId);
    }

    public Chat findByChatId(Long id){
        return chatRepository.findByChatId(id).orElse(null);
    }

    @Transactional
    public void save(Chat chat){
        chatRepository.save(chat);
    }

    public int getMaxCountChannels(){
        return maxCountChannels;
    }
}
