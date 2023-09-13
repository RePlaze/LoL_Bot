package nazenov.functions.Champions;

import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Counters {
    private final TelegramLongPollingBot botInstance;
    private final ThreadPoolExecutor executor;

    public Counters(TelegramLongPollingBot bot) {
        this.botInstance = bot;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool( 5 );
    }

    public void counters(String chatId, String championName) {
        String championUrl = "https://u.gg/lol/champions/" + championName + "/counter?region=ru";

        CompletableFuture.runAsync( () -> {
            try {
                Document document = Jsoup.connect( championUrl ).get();

                // Find the main table section with counters
                Element mainSection = document.select( "div.counters-list.best-win-rate" ).first();
                if (mainSection != null) {
                    Elements counterElements = mainSection.select( "a.counter-list-card.best-win-rate" );

                    // Process the counter elements
                    List<String> countersList = new ArrayList<>();
                    for (Element counterElement : counterElements) {
                        String counterName = counterElement.text();

                        int insertIndex = counterName.indexOf( ' ' ) + 1;

                        if (insertIndex >= 0 && insertIndex < counterName.length()) {
                            counterName = counterName.substring( 0, insertIndex ) + "*" + counterName.substring( insertIndex );
                        }

                        // Find the index of " WR" and extract the portion before it
                        int endIndex = counterName.indexOf( " WR" );
                        if (endIndex >= 0) {
                            counterName = counterName.substring( 0, endIndex );
                        }
                        countersList.add( "*" + counterName );
                    }

                    if (!countersList.isEmpty()) {
                        // Send the counters list as a reply message
                        String formatName = "*" + championName + "*";
                        String replyMessage = "Counters for " + formatName + ":\n" + String.join( "\n", countersList );
                        sendReplyMessage( chatId, replyMessage );
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleException( chatId );
            }
        }, executor );
    }

    public void sendReplyMessage(String chatId, String message) {
        CompletableFuture.runAsync( () -> {
            SendMessage sendMessage = new SendMessage();
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
