package nazenov;

import nazenov.functions.Champions.Build;
import nazenov.functions.Champions.Counters;
import nazenov.functions.Champions.utils.ChampionInfo;
import nazenov.functions.NewSkins;
import nazenov.functions.News;
import nazenov.functions.PatchDate;
import nazenov.utils.OptionBar;
import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "LoL_infoBot";
    }

    @Override
    public String getBotToken() {
        return "6699616361:AAG03q66m9ibreMrjWtjTaYT52ioOuytFwI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText().toLowerCase();
            String chatId = message.getChatId().toString();

            switch (text) {
                case "/start", "back" ->
                        TelegramBotUtil.sendFormattedText( this, message.getChatId().toString(), "*Choose an option*", true, OptionBar.buildKeyboard() );
                case "new skins" -> CompletableFuture.runAsync( () -> new NewSkins( this ).sendNewSkins( chatId ) );
                case "patch dates" -> CompletableFuture.runAsync( () -> new PatchDate( this ).patches( chatId ) );
                case "champion info" ->
                        CompletableFuture.runAsync( () -> new ChampionInfo( this ).selectChampion( chatId ) );
                case "news" -> CompletableFuture.runAsync( () -> new News( this ).checkNews( chatId ) );
                case "exit" -> System.exit( 0 );
                default -> CompletableFuture.runAsync( () -> new ChampionInfo( this ).handleUserInput( chatId, text ) );
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();

            if (data.startsWith( "view_builds:" ) || data.startsWith( "view_counters:" )) {
                String[] parts = data.split( ":" );
                String chatId = parts[1];
                String championName = parts[2];

                if (data.startsWith( "view_builds:" ))
                    new Build( this ).build( chatId, championName );
                else
                    new Counters( this ).counters( chatId, championName );


                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setCallbackQueryId( callbackQuery.getId() );
                try {
                    execute( answerCallbackQuery );
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}