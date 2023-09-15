package nazenov.functions;

import nazenov.utils.SendMessageUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Guide {
    private final ExecutorService executor;
    private final SendMessageUtil sendMessageUtil;

    public Guide(TelegramLongPollingBot bot) {
        this.executor = Executors.newFixedThreadPool( 10 );
        this.sendMessageUtil = new SendMessageUtil( bot, executor );
    }

    public void guide(String chatId, String championName) {
        String searchUrl = "https://yandex.ru/search/?text=+3+Minute+" + championName + "+Guide+youtube";
        System.out.println( searchUrl );

        try {
            Document searchResults = Jsoup.connect( searchUrl ).get();
            Elements links = searchResults.select( "a[href]" );

            for (Element link : links) {
                String href = link.attr( "href" );
                if (href.startsWith( "https://www.youtube.com/watch?" )) {
                    CompletableFuture.runAsync( () -> {
                        try {
                            sendMessageUtil.sendReplyMessage( chatId, "[Guide for " + championName + "](" + href + ")", false, false );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, executor );
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}