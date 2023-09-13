package nazenov.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SendMessage {

    private final TelegramLongPollingBot botInstance;
    private final ExecutorService executor;

    public SendMessage(TelegramLongPollingBot botInstance, ExecutorService executor) {
        this.botInstance = botInstance;
        this.executor = executor;
    }

    public void sendReplyMessage(String chatId, String message) {
        CompletableFuture.runAsync( () -> {
            org.telegram.telegrambots.meta.api.methods.send.SendMessage sendMessage = new org.telegram.telegrambots.meta.api.methods.send.SendMessage();
            sendMessage.setChatId( chatId );
            sendMessage.setText( message );
            sendMessage.enableMarkdown( true );

            try {
                botInstance.execute( sendMessage );
            } catch (TelegramApiException e) {
                e.printStackTrace();
                handleException( chatId );
            }
        }, executor );
    }

    private void handleException(String chatId) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "Error", false, null );
    }
}