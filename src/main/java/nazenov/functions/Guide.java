package nazenov.functions;

import nazenov.utils.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Guide {
    private final TelegramLongPollingBot botInstance;
    private final ExecutorService executor;
    private final SendMessage sendMessage;

    public Guide(TelegramLongPollingBot bot) {
        this.botInstance = bot;
        this.executor = Executors.newFixedThreadPool( 5 );
        this.sendMessage = new SendMessage( bot, executor );
    }

    public void combo(String chatId, String championName) {
        String url = "https://app.mobalytics.gg/lol/champions/" + championName.toLowerCase() + "/combos";

        CompletableFuture.runAsync( () -> {
            sendReplyMessage( chatId, "``` Best combo for ``` *" + championName + "*\n" + url );
        }, executor );
    }

    public void sendReplyMessage(String chatId, String message) {
        sendMessage.sendReplyMessage( chatId, message );
    }
}
