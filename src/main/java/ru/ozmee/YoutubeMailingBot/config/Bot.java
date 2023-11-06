package ru.ozmee.YoutubeMailingBot.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ozmee.YoutubeMailingBot.IOMessage.MessageHandler;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    private final MessageHandler messageHandler;

    @Value("${bot.name}")
    @Getter
    private String botUsername;

    @Value("${bot.token}")
    @Getter
    private String botToken;

    public Bot(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            SendMessage sendMessage = messageHandler.distributeMessage(message);
            executeMessage(sendMessage);
        } else if (update.hasChannelPost() && update.getChannelPost().hasText()) {
            Message message = update.getChannelPost();
            SendMessage sendMessage = messageHandler.distributeMessage(message);
            executeMessage(sendMessage);
        } else if (update.hasCallbackQuery()) {
            EditMessageText edit = messageHandler.deleteRequestCommand(update);
            executeMessage(edit);
        }
    }

    void executeMessage(SendMessage sendMessage){
        try{
            execute(sendMessage);
        } catch (TelegramApiException e){
            e.printStackTrace();
            log.error("Ошибка отправки сообщения");
        }
    }

    void executeMessage(EditMessageText editMessageText){
        try{
            execute(editMessageText);
        } catch (TelegramApiException e){
            e.printStackTrace();
            log.error("Ошибка отправки сообщения");
        }
    }
}
