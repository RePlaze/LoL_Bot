package nazenov.functions.Champions.findChamp;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ChampOption {
    public static InlineKeyboardMarkup championOptionsKeyboard(String chatId, String championName) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        addInlineButton( rows, "Builds", "view_builds", chatId, championName );
        addInlineButton( rows, "Counters", "view_counters", chatId, championName );

        // Modify the "Guide" button to open a website link
        addInlineButtonWithLink( rows, "Abilities",
                "https://app.mobalytics.gg/lol/champions/" + championName.toLowerCase() + "/combos", chatId );

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

    private static void addInlineButtonWithLink(List<List<InlineKeyboardButton>> rows, String buttonText, String url, String chatId) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText( buttonText );
        button.setUrl( url );

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add( button );
        rows.add( row );
    }
}