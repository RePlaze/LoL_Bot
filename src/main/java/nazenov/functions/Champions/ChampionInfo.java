package nazenov.functions.Champions;

import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;

public class ChampionInfo {
    private final TelegramLongPollingBot botInstance;

    public ChampionInfo(TelegramLongPollingBot bot) {
        this.botInstance = bot;
    }

    public void champOptions(String chatId, String championName) {
        CompletableFuture.runAsync( () -> {
            try {
                String imageUrl = getChampionImage( championName );
                String formattedChampionName = "*" + championName + "*";

                SendPhoto sendPhoto = new SendPhoto( chatId, new InputFile( imageUrl ) );
                sendPhoto.setCaption( "Build & Counters for " + formattedChampionName );
                sendPhoto.setParseMode( "Markdown" );
                sendPhoto.setReplyMarkup( ChampOption.championOptionsKeyboard( chatId, championName ) );

                botInstance.execute( sendPhoto );
            } catch (TelegramApiException e) {
                handleTelegramApiException( e, chatId );
            }
        } );
    }

    public void handleCallbackQuery(String chatId, String callbackData) {
        String[] parts = callbackData.split( ":" );
        if (parts.length == 3) {
            String action = parts[0];
            String chatIdFromCallback = parts[1];
            String championName = parts[2];

            if (action.equals( "view_builds" )) {
                // Handle "View Builds" action here
                Build build = new Build( botInstance );
                build.build( chatIdFromCallback, championName );
            } else if (action.equals( "view_counters" )) {
                // Handle "View Counters" action here
                // Implement the logic for viewing counters
            }
        }
    }

    public void selectChampion(String chatId) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "*Name of Champion?*", true, null );
    }

    private String getChampionImage(String championName) {
        return "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + championName.replaceAll( "'", "" ) + "_0.jpg";
    }

    public void handleUserInput(String chatId, String messageText) {
        String championName = messageText.substring( 0, 1 ).toUpperCase() + messageText.substring( 1 );

        // Check if the champion name is valid or suggest similar names
        SearchName searchName = new SearchName();
        String suggestion = searchName.findBestMatch( championName );
        if (!suggestion.equalsIgnoreCase( championName )) {
            TelegramBotUtil.sendFormattedText( botInstance, chatId,
                    "*" + championName + "* not found. Did you mean *" + suggestion + "*?", true, null );
        } else {
            champOptions( chatId, championName );
        }
    }

    public void handleTelegramApiException(TelegramApiException e, String chatId) {
        e.printStackTrace();
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "An error occurred while sending the photo.", false, null );
    }
}
