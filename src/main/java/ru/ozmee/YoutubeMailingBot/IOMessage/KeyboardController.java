package ru.ozmee.YoutubeMailingBot.IOMessage;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.ozmee.YoutubeMailingBot.models.Channel;
import ru.ozmee.YoutubeMailingBot.util.ConverterHttp;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardController {
    private final ConverterHttp converterHttp;

    public KeyboardController(ConverterHttp converterHttp) {
        this.converterHttp = converterHttp;
    }

    InlineKeyboardMarkup registerInlineKeyboard(List<Channel> channels){

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        for (Channel channel : channels) {
            var button = new InlineKeyboardButton();
            List<InlineKeyboardButton> rowInLine = new ArrayList<>();

            button.setText("Удалить: " + converterHttp.getAuthorOfChannel(channel));
            button.setCallbackData(Integer.toString(channel.getId()));
            rowInLine.add(button);
            rowsInLine.add(rowInLine);
        }
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    ReplyKeyboardMarkup registerReplyKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/my_channels");
        row.add("/start");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }
}
