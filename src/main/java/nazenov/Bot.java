package nazenov;

import nazenov.functions.Champions.Build;
import nazenov.functions.Champions.ChampionInfo;
import nazenov.functions.Champions.Counters;
import nazenov.functions.NewSkins;
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
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText().toLowerCase();

            switch (text) {
                case "/start", "back" ->
                        TelegramBotUtil.sendFormattedText( this, message.getChatId().toString(), "*Choose an option*", true, OptionBar.buildKeyboard() );
                case "new skins" ->
                        CompletableFuture.runAsync( () -> new NewSkins( this ).sendNewSkins( message.getChatId().toString() ) );
                case "patch dates" ->
                        CompletableFuture.runAsync( () -> new PatchDate( this ).patches( message.getChatId().toString() ) );
                case "champion info" ->
                        CompletableFuture.runAsync( () -> new ChampionInfo( this ).selectChampion( message.getChatId().toString() ) );
                case "exit" -> System.exit( 0 );
                default ->
                        CompletableFuture.runAsync( () -> new ChampionInfo( this ).handleUserInput( message.getChatId().toString(), text ) );
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