package nazenov.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class OptionBar {

    public static ReplyKeyboardMarkup buildKeyboard() {
        ReplyKeyboardMarkup options = new ReplyKeyboardMarkup();
        options.setSelective(true);
        options.setResizeKeyboard(true);
        options.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRow.add("New Skins");
        keyboardRow.add("Patch Dates");

        keyboard.add(keyboardRow);

        options.setKeyboard(keyboard);

        return options;
    }
}

