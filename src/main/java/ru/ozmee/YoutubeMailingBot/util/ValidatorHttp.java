package ru.ozmee.YoutubeMailingBot.util;

import org.springframework.stereotype.Component;

@Component
public class ValidatorHttp {
    public boolean isValidHttp(String http){
        return http.matches("^https://www.youtube.com/@.+") || http.matches("^https://youtube.com/@.+");
    }

    public boolean isHttp(String http){
        return http.matches("https://.+");
    }
}
