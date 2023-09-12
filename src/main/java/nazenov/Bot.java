package nazenov;

import nazenov.functions.Champions.Build;
import nazenov.functions.Champions.ChampionInfo;
import nazenov.functions.NewSkins;
import nazenov.functions.PatchDate;
import nazenov.utils.OptionBar;
import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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
            // Handle text messages as before
            Message message = update.getMessage();
            String text = message.getText();

            switch (text.toLowerCase()) {
                case "/start", "back" -> {
                    ReplyKeyboardMarkup options = OptionBar.buildKeyboard();
                    TelegramBotUtil.sendFormattedText( this, message.getChatId().toString(), "*Choose an option*", true, options );
                }
                case "new skins" ->
                        CompletableFuture.runAsync( () -> new NewSkins( this ).sendNewSkins( message.getChatId().toString() ) );
                case "patch dates" ->
                        CompletableFuture.runAsync( () -> new PatchDate( this ).patches( message.getChatId().toString() ) );
                case "champion info" ->
                        CompletableFuture.runAsync( () -> new ChampionInfo( this ).selectChampion( message.getChatId().toString() ) );
                default ->
                        CompletableFuture.runAsync( () -> new ChampionInfo( this ).handleUserInput( message.getChatId().toString(), text ) );
            }
        } else if (update.hasCallbackQuery()) {
            // Handle button callbacks
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();

            if (data.startsWith( "view_builds:" )) {
                String[] parts = data.split( ":" );
                String chatId = parts[1];
                String championName = parts[2];

                Build build = new Build( this );
                build.build( chatId, championName );
            } else if (data.startsWith( "view_counters:" )) {

            }

            // Send a callback query response (acknowledge the button press)
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
