package nazenov.functions.Champions.findChamp;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ChampionKeyboad {
    public static ReplyKeyboardMarkup championButtons() {
        // Create a custom keyboard
        ReplyKeyboardMarkup k = new ReplyKeyboardMarkup();
        k.setSelective( true );
        k.setResizeKeyboard( true );
        k.setOneTimeKeyboard( false );

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add( "Back" );

        keyboard.add( keyboardRow );
        k.setKeyboard( keyboard );
        return k;
    }

    public static ReplyKeyboardMarkup yesButton(String s) {
        // Create a custom keyboard
        ReplyKeyboardMarkup k = new ReplyKeyboardMarkup();
        k.setSelective( true );
        k.setResizeKeyboard( true );
        k.setOneTimeKeyboard( false );

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add( s );

        keyboard.add( keyboardRow );
        k.setKeyboard( keyboard );
        return k;
    }
}
