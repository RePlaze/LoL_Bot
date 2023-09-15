package nazenov.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;

public class SendMessageUtil {
    private final TelegramLongPollingBot bot;
    private final ExecutorService executor;

    public SendMessageUtil(TelegramLongPollingBot bot, ExecutorService executor) {
        this.bot = bot;
        this.executor = executor;
    }

    public void sendReplyMessage(String chatId, String message, boolean disableWebPagePreview, boolean disableNotification) {
        executor.execute( () -> {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId( chatId );
            sendMessage.setText( message );
            sendMessage.setDisableWebPagePreview( disableWebPagePreview );
            sendMessage.setDisableNotification( disableNotification );
            sendMessage.enableMarkdown( true );
            try {
                Message response = bot.execute( sendMessage );
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } );
    }
}

