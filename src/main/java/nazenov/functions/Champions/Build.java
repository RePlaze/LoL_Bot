package nazenov.functions.Champions;

import nazenov.utils.SendMessage;
import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Build {
    private final TelegramLongPollingBot botInstance;
    private final ExecutorService executor;
    private final SendMessage sendMessage;


    public Build(TelegramLongPollingBot bot) {
        this.botInstance = bot;
        this.executor = Executors.newFixedThreadPool( 5 );
        this.sendMessage = new SendMessage( bot, executor );
    }

    public void build(String chatId, String championName) {
        String championUrl = "https://www.mobafire.com/league-of-legends/champion/" + championName;

        CompletableFuture.runAsync( () -> {
            try {
                Document document = Jsoup.connect( championUrl ).get();
                Elements tables = document.select( "div.champ-build__section__content" );

                if (!tables.isEmpty()) {
                    List<String> tableTexts = new ArrayList<>();

                    String[] prefixes = {"`Runes`: ", "`Summoners`: ", "``First Buy``: ", "`Main`", "",
                            "`Last item`: "};

                    for (int i = 0; i < Math.min( tables.size(), prefixes.length ); i++) {
                        Element table = tables.get( i );
                        String tableText = table.text()
                                .replaceAll( "\\bSecond\\b|\\bBest\\b|\\bWin Rate\\b|\\d+|%", "" )
                                .replaceAll( "\\bPickrate\\b", "\n" );

                        if (i == 0 || i == 1) {
                            String[] words = tableText.split( "\\s+" );
                            if (words.length > 2)
                                tableText = words[0] + " " + words[1];
                        } else if (i == 2) {
                            String[] lines = tableText.split( "\n" );
                            if (lines.length > 0)
                                tableText = lines[0];
                        } else if (i == 3) {
                            String[] lines = tableText.split( "\n" );
                            StringBuilder modifiedText = new StringBuilder();
                            for (String line : lines) {
                                modifiedText.append( "`Build`: " ).append( line ).append( "\n" );
                            }
                            tableText = modifiedText.toString();
                        }
                        tableText = tableText.replaceAll( "\\bThird\\b", "" );
                        if (!tableText.isEmpty()) {
                            tableTexts.add( prefixes[i] + tableText );
                        }
                    }

                    String replyMessage = String.join( "\n\n", tableTexts );
                    String formatName = "*" + championName + "*";
                    replyMessage = "Build for " + formatName + "\n\n" + replyMessage;
                    sendReplyMessage( chatId, replyMessage );
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleException( chatId );
            }
        }, executor );
    }

    public void sendReplyMessage(String chatId, String message) {
        sendMessage.sendReplyMessage( chatId, message ); // Use the sendMessageUtil instance
    }

    private void handleException(String chatId) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "An error occurred.", false, null );
    }
}
