package ru.ozmee.YoutubeMailingBot.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ozmee.YoutubeMailingBot.models.Channel;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConverterHttpTest {
    private final ConverterHttp converterHttp;

    ConverterHttpTest() {
        this.converterHttp = new ConverterHttp();
    }

    @Test
    void convertVideosHttpToLastVideoHttp() {
        assertEquals("https://www.youtube.com/watch?v=jcsG-IlJ-SA", converterHttp.convertVideosHttpToLastVideoHttp("https://www.youtube.com/@wndtn/videos"));
    }
    @Test
    void convertHttpToVideosHttp(){
        assertEquals("https://www.youtube.com/@wndtn/videos", converterHttp.convertHttpToVideosHttp("https://www.youtube.com/@wndtn/featured"));
        assertEquals("https://www.youtube.com/@wndtn/videos", converterHttp.convertHttpToVideosHttp("https://www.youtube.com/@wndtn"));
        assertEquals("https://www.youtube.com/@MrMarmok/videos", converterHttp.convertHttpToVideosHttp("https://youtube.com/@MrMarmok?si=dMPketRHE1JgwrtO"));
    }

    @Test
    void getAuthorOfChannel(){
        Channel channel = new Channel();
        channel.setVideosHttp("https://www.youtube.com/@AriaNightcore/videos");
        assertEquals("AriaNightcore", converterHttp.getAuthorOfChannel(channel));
        channel.setVideosHttp("https://www.youtube.com/@MrMarmok?si=dMPketRHE1JgwrtO");
        assertEquals("MrMarmok", converterHttp.getAuthorOfChannel(channel));
    }
}