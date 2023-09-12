package nazenov;

import nazenov.functions.Champions.ChampionInfo;
import nazenov.functions.NewSkins;
import nazenov.functions.PatchDate;
import nazenov.utils.OptionBar;
import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

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
            String text = message.getText();

            switch (text.toLowerCase()) {
                case "/start", "back" -> {
                    ReplyKeyboardMarkup options = OptionBar.buildKeyboard();
                    TelegramBotUtil.sendFormattedText( this, message.getChatId().toString(), "*Choose option*", true, options );
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
        }
    }
}