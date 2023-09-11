package nazenov.functions.Champions;

import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static nazenov.functions.Champions.ChampionKeyboad.championButtons;

public class ChampionInfo {
    private static TelegramLongPollingBot bot = null;
    private static final String lastMessage = "";

    public ChampionInfo(TelegramLongPollingBot bot) {
        ChampionInfo.bot = bot;
    }

    public void champOptions(String chatId, String championName) {

        String imageUrl = getChampionImage( championName );
        String formattedChampionName = "*" + championName + "*";

        // Create a SendPhoto object to send the image
        SendPhoto sendPhoto = new SendPhoto( chatId, new InputFile( imageUrl ) );
        sendPhoto.setCaption( "Choose an option for " + formattedChampionName );
        sendPhoto.setParseMode( "Markdown" );
        sendPhoto.setReplyMarkup( championButtons() );

        try {
            // Send the photo with the custom keyboard
            bot.execute( sendPhoto );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void selectChampion(String chatId) {
        // Send a message asking for the champion's name
        TelegramBotUtil.sendFormattedText( bot, chatId, "*Name of Champion?*", true, null );
    }

    private String getChampionImage(String championName) {
        return "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + championName.replaceAll( "'", "" ) + "_0.jpg";
    }

    public void handleUserInput(String chatId, String messageText) {
        String championName = messageText.substring( 0, 1 ).toUpperCase() + messageText.substring( 1 );
        champOptions( chatId, championName );
    }

}
