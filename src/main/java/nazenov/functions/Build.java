package nazenov.functions;

import nazenov.utils.SendMessage;
import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Build {
    private final TelegramLongPollingBot botInstance;
    private final SendMessage sendMessage;
    private final String[] prefixes = {"`Runes`: ", "`Summoners`: ", "``First Buy``: ", "`Main`", "", "`Last item`: "};
    private Elements tables;

    public Build(TelegramLongPollingBot bot) {
        this.botInstance = bot;
        ExecutorService executor = Executors.newFixedThreadPool( 10 );
        this.sendMessage = new SendMessage( bot, executor );
    }

    public void build(String chatId, String championName) {
        try {
            String championUrl = "https://www.mobafire.com/league-of-legends/champion/" + championName;
            Document document = Jsoup.connect( championUrl ).get();
            tables = document.select( "div.champ-build__section__content" );

            List<String> tableTexts = tables.stream()
                    .limit( 6 )
                    .map( this::processTable )
                    .filter( text -> !text.isEmpty() )
                    .collect( Collectors.toList() );

            if (!tableTexts.isEmpty()) {
                String replyMessage = "Build for *" + championName + "*\n\n" +
                        String.join( "\n\n", tableTexts );
                sendReplyMessage( chatId, replyMessage );
            }
        } catch (IOException e) {
            e.printStackTrace();
            handleException( chatId );
        }
    }

    private String processTable(Element table) {
        String tableText = table.text();
        int tableIndex = tables.indexOf( table );

        if (tableIndex < 0)
            return "";

        if (tableIndex == 0 || tableIndex == 1) {
            String[] words = tableText.split( "\\s+" );
            if (words.length > 2)
                tableText = words[0] + " " + words[1];
        } else if (tableIndex == 2) {
            String[] lines = tableText.split( "\n" );
            if (lines.length > 0)
                tableText = lines[0];
        } else if (tableIndex == 3) {
            String[] lines = tableText.split( "\n" );
            tableText = "`Build`: " + String.join( "\n`Build`: ", lines );
        }

        tableText = tableText.replaceAll( "\\bSecond\\b|\\bBest\\b|\\bWin Rate\\b|\\d+|%", "" )
                .replaceAll( "\\bPickrate\\b", "\n" )
                .replaceAll( "\\bThird\\b", "" );

        return prefixes[tableIndex] + tableText;
    }

    public void sendReplyMessage(String chatId, String message) {
        sendMessage.sendReplyMessage( chatId, message );
    }

    private void handleException(String chatId) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "An error occurred.", false, null );
    }
}