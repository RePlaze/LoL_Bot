package nazenov.functions;

import nazenov.utils.SendMessage;
import nazenov.utils.TelegramBotUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Counters {
    private final TelegramLongPollingBot botInstance;
    private final ExecutorService executor;
    private final SendMessage sendMessage;

    public Counters(TelegramLongPollingBot bot) {
        this.botInstance = bot;
        this.executor = Executors.newFixedThreadPool( 10 );
        this.sendMessage = new SendMessage( bot, executor );
    }

    public void counters(String chatId, String championName) {
        CompletableFuture.runAsync( () -> {
            try {
                long startTime = System.currentTimeMillis();
                String url = "https://u.gg/lol/champions/" + championName + "/counter";

                List<String> bestCounters = fetchCounterList( url, "best-win-rate", "Lost against" );
                List<String> worstCounters = fetchCounterList( url, "worst-win-rate", "Wins against" );

                sendCounterMessages( chatId, championName, bestCounters, worstCounters );

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                System.out.println( "Method execution time: " + executionTime + " milliseconds" );
            } catch (IOException e) {
                handleException( chatId );
            }
        }, executor );
    }

    private List<String> fetchCounterList(String url, String cssClass, String messagePrefix) throws IOException {
        Connection connection = Jsoup.connect( url );
        connection.userAgent( "Mozilla/5.0" );

        Document document = connection.get();
        Elements mainSection = document.select( "div.counters-list." + cssClass );
        Elements elements = mainSection.select( "a.counter-list-card." + cssClass );

        return elements.stream().map( this::formatCounterName ).collect( Collectors.toList() );
    }

    private String formatCounterName(Element counterElement) {
        String counterName = counterElement.text();
        int insertIndex = counterName.indexOf( ' ' ) + 1;

        if (insertIndex < counterName.length())
            counterName = counterName.substring( 0, insertIndex ) + "*" + counterName.substring( insertIndex );
        counterName = counterName.substring( 0, counterName.indexOf( " WR" ) + 3 );

        return "*" + counterName;
    }

    private void sendCounterMessages(String chatId, String championName, List<String> bestCounters, List<String> worstCounters) {
        if (!bestCounters.isEmpty() || !worstCounters.isEmpty()) {
            String formatName = "*" + championName + "*";
            String bestCounterMessage = formatName + " ``` Lost against```:\n" + String.join( "\n", bestCounters );
            String worstCounterMessage = formatName + " ``` Wins against```:\n" + String.join( "\n", worstCounters );

            sendReplyMessage( chatId, bestCounterMessage );
            sendReplyMessage( chatId, worstCounterMessage );
        }
    }

    public void sendReplyMessage(String chatId, String message) {
        sendMessage.sendReplyMessage( chatId, message );
    }

    private void handleException(String chatId) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "Error", false, null );
    }
}
