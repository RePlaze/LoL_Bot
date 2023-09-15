package nazenov.functions.Champions.findChamp;

import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChampionInfo {
    private final TelegramLongPollingBot botInstance;
    private final ExecutorService executorService;

    public ChampionInfo(TelegramLongPollingBot bot) {
        this.botInstance = bot;
        this.executorService = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
    }

    public void champOptions(String chatId, String championName) {
        CompletableFuture.runAsync( () -> {
            try {
                String imageUrl = getChampionImage( championName );

                SendPhoto sendPhoto = new SendPhoto( chatId, new InputFile( imageUrl ) );
                sendPhoto.setCaption( "``` select option for``` *" + championName + "*" );
                sendPhoto.setParseMode( "Markdown" );
                sendPhoto.setReplyMarkup( ChampOption.championOptionsKeyboard( chatId, championName ) );

                botInstance.execute( sendPhoto );
            } catch (TelegramApiException e) {
                handleTelegramApiException( e, chatId );
            }
        }, executorService );
    }

    public void selectChampion(String chatId) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "*Name of Champion?*", true, ChampionKeyboad.championButtons() );
    }

    private String getChampionImage(String championName) {
        return String.format( "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_0.jpg", championName.replaceAll( "'", "" ) );
    }

    public void handleUserInput(String chatId, String messageText) {
        String championName = messageText.substring( 0, 1 ).toUpperCase() + messageText.substring( 1 );
        SearchName searchName = new SearchName();
        String suggestion = searchName.findBestMatch( championName );

        if (!suggestion.equalsIgnoreCase( championName ))
            TelegramBotUtil.sendFormattedText( botInstance, chatId,
                    "*" + championName + "* not found. Did you mean *" + suggestion + "*?", true, null );
        else
            champOptions( chatId, championName );
    }

    public void handleTelegramApiException(TelegramApiException e, String chatId) {
        e.printStackTrace();
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "Error sending the photo.", false, null );
    }
}
