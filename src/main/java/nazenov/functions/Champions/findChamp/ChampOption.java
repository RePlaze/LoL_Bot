package nazenov.functions.Champions.findChamp;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ChampOption {
    public static InlineKeyboardMarkup championOptionsKeyboard(String chatId, String championName) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Helper function to create and add a button to a row
        addInlineButton( rows, "Builds", "view_builds", chatId, championName );
        addInlineButton( rows, "Counters", "view_counters", chatId, championName );

        keyboardMarkup.setKeyboard( rows );
        return keyboardMarkup;
    }

    private static void addInlineButton(List<List<InlineKeyboardButton>> rows, String buttonText, String callbackData, String chatId, String championName) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText( buttonText );
        button.setCallbackData( callbackData + ":" + chatId + ":" + championName );

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add( button );

        rows.add( row );
    }
}
