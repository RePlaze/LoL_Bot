package nazenov.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotUtil {
    public static void sendFormattedText(AbsSender sender, String chatId, String text, boolean markdown, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage(chatId, text);
        message.enableMarkdown(markdown);
        if (keyboardMarkup != null) {
            message.setReplyMarkup(keyboardMarkup);
        }
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
