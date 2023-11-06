package ru.ozmee.YoutubeMailingBot.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="chat")
public class Chat {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "chat_id_into_telegram")
    private long chatId;

    @ManyToMany(mappedBy = "chats", fetch = FetchType.EAGER)
    private List<Channel> channels = new ArrayList<>();

    public void addChannel(Channel channel){
        this.channels.add(new Channel(channel.getId()));
    }

    public Chat() {}

    public Chat(int id){
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public String toString(){
        return id + ") " + chatId;
    }
}
