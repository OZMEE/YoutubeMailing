package ru.ozmee.YoutubeMailingBot.util;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidatorHttpTest {
    private final ValidatorHttp validatorHttp;

    public ValidatorHttpTest(){
        validatorHttp = new ValidatorHttp();
    }
    @Test
    void isValidHttp() {
        assertTrue(validatorHttp.isValidHttp("https://youtube.com/@MrMarmok?si=dMPketRHE1JgwrtO"));
        assertTrue(validatorHttp.isValidHttp("https://www.youtube.com/@wndtn/videos"));
        assertFalse(validatorHttp.isValidHttp("https://spring.io/guides/gs/testing-web/"));
    }

    @Test
    void isHttp() {
        assertTrue(validatorHttp.isHttp("https://www.youtube.com/watch?v=sv9s3m6ILYc"));
        assertFalse(validatorHttp.isHttp("htt://spring.io/guides/gs/testing-web/"));
    }
}