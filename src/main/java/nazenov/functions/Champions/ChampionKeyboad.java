package nazenov.functions.Champions;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ChampionKeyboad {
    public static ReplyKeyboard championButtons() {
        // Create a custom keyboard
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective( true );
        keyboardMarkup.setResizeKeyboard( true );
        keyboardMarkup.setOneTimeKeyboard( false );

        List<KeyboardRow> keyboard = new ArrayList<>();
//        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();

        // Add buttons to the keyboard rows
        keyboardRow2.add( "Back" );

//        keyboard.add( keyboardRow1 );
        keyboard.add( keyboardRow2 );
        keyboardMarkup.setKeyboard( keyboard );
        return keyboardMarkup;
    }
}
