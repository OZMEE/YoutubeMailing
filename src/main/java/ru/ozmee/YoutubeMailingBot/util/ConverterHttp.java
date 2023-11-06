package ru.ozmee.YoutubeMailingBot.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.ozmee.YoutubeMailingBot.models.Channel;

import java.io.IOException;

@Component
public class ConverterHttp {
    public String convertHttpToVideosHttp(String http) {
        StringBuilder str = new StringBuilder("https://www.youtube.com/");
        int index = http.indexOf("@");
        while (index < http.length() && http.charAt(index) != '/' && http.charAt(index)!='?') {
            str.append(http.charAt(index));
            index++;
        }
        str.append("/videos");
        return str.toString();
    }

    public String convertVideosHttpToLastVideoHttp(String videosHttp) throws NotFoundLastVideoException {
        try {
            String script = takeTheScriptFromHTML(videosHttp);
            int index = script.indexOf("\"url\"");
            StringBuilder str = new StringBuilder();
            //ЭТО ТОЧНО ПРОИЗОЙДЕТ, ЦИКЛ НЕ БЕСКОНЕЧНЫЙ!!!
            if(!script.contains("/watch")){
                throw new Exception();
            }
            while (!str.toString().contains("/watch")) {
                str = new StringBuilder();
                index = script.indexOf("\"url\"", index + 2);
                index += 7;
                while (script.charAt(index) != '"') {
                    str.append(script.charAt(index));
                    index++;
                }
            }
            return "https://www.youtube.com" + str;
        } catch(Exception e){
            throw new NotFoundLastVideoException("Канал с ссылкой: " + videosHttp + " не имеет видео");
        }

    }
    private String takeTheScriptFromHTML(String http) throws IOException {
        Document document = Jsoup.connect(http).get();
        Elements allLinks = document.getElementsByTag("script");
        String script = allLinks.get(allLinks.size()-6).toString();
        return script;
    }
    public String getAuthorOfChannel(Channel channel){
        String videosHttp = channel.getVideosHttp();
        int index = videosHttp.indexOf('@')+1;
        StringBuilder author = new StringBuilder();
        while(videosHttp.charAt(index)!='/' && videosHttp.charAt(index)!='?'){
            author.append(videosHttp.charAt(index));
            index++;
        }

        return author.toString();
    }
}
