package nazenov.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class OptionBar {

    public static ReplyKeyboardMarkup buildKeyboard() {
        ReplyKeyboardMarkup options = new ReplyKeyboardMarkup();
        options.setSelective( true );
        options.setResizeKeyboard( true );
        options.setOneTimeKeyboard( false );

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Create a new KeyboardRow for "Champion Info" button
        KeyboardRow championInfoRow = new KeyboardRow();
        championInfoRow.add( "Champion Info" );
        keyboard.add( championInfoRow );

        // Create a new KeyboardRow for "New Skins" and "Patch Dates" buttons in the same row
        KeyboardRow secondaryRow = new KeyboardRow();
        secondaryRow.add( "New Skins" );
        secondaryRow.add( "News" );
        secondaryRow.add( "Patch Dates" );
        keyboard.add( secondaryRow );

        options.setKeyboard( keyboard );

        return options;
    }
}
