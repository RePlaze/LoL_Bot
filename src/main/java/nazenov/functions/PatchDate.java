package nazenov.functions;

import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PatchDate {
    private final TelegramLongPollingBot bot;
    private static final String WEBPAGE_URL = "https://earlygame.com/lol/2023-patch-schedule";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "EEEE, MMM dd, yyyy", Locale.ENGLISH );

    public PatchDate(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void patches(String chatId) {
        try {
            Document document = Jsoup.connect( WEBPAGE_URL ).get();
            Elements rows = document.select( "table tbody tr" );

            List<String> patchInfoList = new ArrayList<>();
            Date today = new Date();

            for (Element row : rows) {
                Elements columns = row.select( "td" );
                if (columns.size() >= 2) {
                    String patchName = columns.get( 0 ).text();
                    String patchDateStr = columns.get( 1 ).text();
                    processPatchInfo( patchInfoList, today, patchName, patchDateStr );
                }
            }

            String responseMessage = formatResponseMessage( patchInfoList );
            TelegramBotUtil.sendFormattedText( bot, chatId, responseMessage, true, null );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processPatchInfo(List<String> patchInfoList, Date today, String patchName, String patchDateStr) {
        if (isValidDate( patchDateStr )) {
            try {
                Date patchDate = DATE_FORMAT.parse( patchDateStr );
                if (patchDate.after( today ))
                    patchInfoList.add( patchName + " -> " + patchDateStr );
                else
                    patchInfoList.add( formatPatchLink( patchName ) + " -> " + patchDateStr );
            } catch (ParseException e) {
                // Handle or log the exception
                e.printStackTrace();
            }
        } else
            patchInfoList.add( formatPatchLink( patchName ) + " ->! " + patchDateStr );
    }

    private boolean isValidDate(String dateStr) {
        DATE_FORMAT.setLenient( false );
        try {
            DATE_FORMAT.parse( dateStr );
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private String formatPatchLink(String patchName) {
        return "[" + patchName + "](https://www.leagueoflegends.com/en-au/news/game-updates/patch-" +
                patchName.replaceAll( "\\.", "-" ).replaceAll( " ", "" ) + "-notes)";
    }

    private String formatResponseMessage(List<String> patchInfoList) {
        return "*Patch Dates:*\n" + patchInfoList.stream()
                .map( patchInfo -> patchInfo
                        .replaceAll( "LoL", "" )
                        .replaceAll( "Patch", "" )
                        .replaceAll( "nesday|rsday|esday", "" )
                )
                .collect( Collectors.joining( "\n" ) );
    }
}
