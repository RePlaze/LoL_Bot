package nazenov.functions.Champions;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ChampOption {
    public static InlineKeyboardMarkup championOptionsKeyboard(String chatId, String championName) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Add "View Builds" button
        InlineKeyboardButton buildButton = new InlineKeyboardButton();
        buildButton.setText( "Builds" );
        buildButton.setCallbackData( "view_builds:" + chatId + ":" + championName ); // Pass chatId and championName as part of callback data
        List<InlineKeyboardButton> buildRow = new ArrayList<>();
        buildRow.add( buildButton );

        // Add "View Counters" button
        InlineKeyboardButton countersButton = new InlineKeyboardButton();
        countersButton.setText( "Counters" );
        countersButton.setCallbackData( "view_counters:" + championName ); // Replace with appropriate callback data
        List<InlineKeyboardButton> countersRow = new ArrayList<>();
        countersRow.add( countersButton );

        rows.add( buildRow );
        rows.add( countersRow );

        keyboardMarkup.setKeyboard( rows );
        return keyboardMarkup;
    }
}