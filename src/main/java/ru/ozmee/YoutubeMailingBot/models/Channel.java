package ru.ozmee.YoutubeMailingBot.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="channel")
public class Channel {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="last_video_http")
    private String lastVideoHttp;

    @Column(name="videos_http")
    private String videosHttp;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name= "CHANNEL_CHAT",
            joinColumns =  @JoinColumn(name="channel_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="chat_id", referencedColumnName = "id")
    )
    private List<Chat> chats = new ArrayList<>();

    public void addChat(Chat chat){
        this.chats.add(new Chat(chat.getId()));
    }

    public Channel(){
    }

    public Channel(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideosHttp() {
        return videosHttp;
    }

    public void setVideosHttp(String http) {
        this.videosHttp = http;
    }

    public String getLastVideoHttp() {
        return lastVideoHttp;
    }

    public void setLastVideoHttp(String lastVideoHttp) {
        this.lastVideoHttp = lastVideoHttp;
    }
    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public String toString(){
        return id+") " + videosHttp + ", " + lastVideoHttp;
    }
}
