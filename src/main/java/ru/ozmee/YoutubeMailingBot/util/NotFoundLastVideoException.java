package ru.ozmee.YoutubeMailingBot.util;

public class NotFoundLastVideoException extends RuntimeException{
    public NotFoundLastVideoException(String msg){
        super(msg);
    }
}
