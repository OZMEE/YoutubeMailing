package ru.ozmee.YoutubeMailingBot.IOMessage;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.ozmee.YoutubeMailingBot.models.Channel;
import ru.ozmee.YoutubeMailingBot.models.Chat;
import ru.ozmee.YoutubeMailingBot.services.ChannelService;
import ru.ozmee.YoutubeMailingBot.services.ChatService;
import ru.ozmee.YoutubeMailingBot.util.ConverterHttp;
import ru.ozmee.YoutubeMailingBot.util.NotFoundLastVideoException;
import ru.ozmee.YoutubeMailingBot.util.ValidatorHttp;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class MessageHandler {
    private final ChannelService channelService;
    private final ChatService chatService;
    private final KeyboardController keyboardController;
    private final ValidatorHttp validatorHttp;
    private final ConverterHttp converterHttp;
    @Autowired
    public MessageHandler(ChannelService channelService, ChatService chatService, KeyboardController keyboardController, ValidatorHttp validatorHttp, ConverterHttp converterHttp){
        this.channelService = channelService;
        this.chatService = chatService;
        this.keyboardController = keyboardController;
        this.validatorHttp = validatorHttp;
        this.converterHttp = converterHttp;
    }

    private static final String START = "/start";
    private static final String MY_CHANNELS = "/my_channels";

    public SendMessage distributeMessage(Message messageFromUser){
        String textFromUser = messageFromUser.getText();
        Long chatId = messageFromUser.getChatId();
        String username = messageFromUser.getChat().getUserName()==null ? "Таинственный Гигачадурл" : '@' + messageFromUser.getChat().getUserName();
        chatService.ifNotExistCreate(chatId);

        SendMessage message;

        if ( validatorHttp.isHttp(textFromUser) ){
            if (validatorHttp.isValidHttp(textFromUser)){
                message = regNewChannelCommand(chatId, username, textFromUser);
            } else{
                message = defaultMessage(chatId, "Не корректная ссылка");
            }
        }
        else {
            switch (textFromUser) {
                case START -> message = startMessage(chatId, username);
                case MY_CHANNELS -> message = myChannelsMessage(chatId, username);
                default -> message = defaultMessage(chatId, "Такой команды не существует");
            }
        }
        return message;
    }

    public EditMessageText deleteRequestCommand(Update update){
        String response = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String username = update.getCallbackQuery().getFrom().getUserName();

        String text;

        int channelId = Integer.parseInt(response);

        EditMessageText editMessage = new EditMessageText();

        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setMessageId((int)messageId);

        if(chatService.chatHaveChannel(chatId, channelId)) {
            chatService.deleteChannelFromChat(chatId, channelId);
            channelService.deleteChannelIfNotUse(channelId);
            List<Channel> channels = chatService.findByChatId(chatId).getChannels();

            InlineKeyboardMarkup inlineKeyboard = keyboardController.registerInlineKeyboard(channels);
            editMessage.setReplyMarkup(inlineKeyboard);

            text = generateTextMyChannels(channels, username);
        } else{
            text = "Данные в этом сообщение уже не действительные, откройте новый список каналов";
        }

        editMessage.setText(text);

        return editMessage;
    }

    private SendMessage startMessage(Long chatId, String username){
        String text = """
                Привет, %s \uD83E\uDD1A
                Я бот, который поможет вам не пропустить новые видео от интересующих вас каналов 👍
                Настроить меня можно очень легко, в вашем распоряжении всего одна кнопка - /my_channels
                С помощью нее вы можете посмотреть список каналов, там же вы можете удалять каналы из списка, с помощью прикрепленнных кнопок
                Чтобы добавить канал вам необходимо кинуть ссылку на канал
                Ссылка может быть на любой раздел канала, например: "главная", "видео", "трансляции" и т.п.🧐
                """;
        String formattedText = EmojiParser.parseToUnicode(String.format(text, username));

        return new SendMessage(Long.toString(chatId), formattedText);
    }

    private SendMessage regNewChannelCommand(Long chatId, String username, String requestHttp){

        if (!chatService.isValidCountChannels(chatId)){
            return defaultMessage(chatId, "В этом чате уже добавлено максимальное кол-во каналов(");
        }

        String lastVideoHttp;
        String videosHttp;
        try{
            videosHttp = converterHttp.convertHttpToVideosHttp(requestHttp);
            lastVideoHttp= converterHttp.convertVideosHttpToLastVideoHttp(videosHttp);
        }catch (NotFoundLastVideoException e){
            return defaultMessage(chatId, e.getMessage());
        }

        String text;
        Channel channel = channelService.findByVideosHttpOrElseCreate(videosHttp, lastVideoHttp);
        List<Chat> chats = channel.getChats();

        if(chats == null || chats.stream().noneMatch(chat -> Objects.equals(chat.getChatId(), chatId))){
            text = String.format("%s, канал успешно добавлен", username);
            Chat chat = chatService.findByChatId(chatId);
            chat.addChannel(channel);
            channel.addChat(chat);
            chatService.save(chat);
            channelService.save(channel);
        } else{
            text = String.format("%s, этот канал уже добавлен в список", username);
        }

        return defaultMessage(chatId, text);
    }

    private String generateTextMyChannels(List<Channel> channels, String username){
        StringBuilder text = new StringBuilder("🤩@" + username);

        if(!channels.isEmpty()){
            text.append(", уведомления в этом канале: \n");
            text.append(String.format("(%d/%d)\n", channels.size(), chatService.getMaxCountChannels()));
            for (Channel channel : channels) {
                text.append(channel.getVideosHttp()).append("\n");
            }
        } else{
            text.append(", в этом чате пока отсутствуют каналы с уведомлением о выходе новых видео");
        }
        return text.toString();
    }

    private SendMessage defaultMessage(Long chatId, String text){
        String chatIdStr = String.valueOf(chatId);
        String ans = EmojiParser.parseToUnicode(text);
        SendMessage sendMessage = new SendMessage(chatIdStr, ans);

        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardController.registerReplyKeyboard();

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage myChannelsMessage(Long chatId, String username){
        Chat chat = chatService.findByChatId(chatId);
        List<Channel> channels = chat.getChannels();

        String text = generateTextMyChannels(channels, username);

        String chatIdStr = String.valueOf(chatId);
        SendMessage sendMessage = new SendMessage(chatIdStr, text);

        InlineKeyboardMarkup markupInLine = keyboardController.registerInlineKeyboard(channels);
        sendMessage.setReplyMarkup(markupInLine);

        return sendMessage;
    }

    public String notifyMessage(String author, String http){
        String msg = String.format("\uD83E\uDD73 У %s вышло новое видео: \uD83E\uDD73 \n %s", author, http);
        return EmojiParser.parseToUnicode(msg);
    }
}
