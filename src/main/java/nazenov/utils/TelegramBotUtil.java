package nazenov.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotUtil {
    public static void sendFormattedText(AbsSender sender, String chatId, String text, boolean markdown, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage( chatId, text );
        message.enableMarkdown( markdown );
        if (keyboardMarkup != null) {
            message.setReplyMarkup( keyboardMarkup );
        }
        try {
            sender.execute( message );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendPhotoWithCaption(
            TelegramLongPollingBot bot,
            String chatId,
            String photoUrl,
            String caption,
            ReplyKeyboardMarkup keyboardMarkup
    ) {
        // Create a SendPhoto object to send the image
        SendPhoto sendPhoto = new SendPhoto( chatId, new InputFile( photoUrl ) );
        sendPhoto.setCaption( caption );

        if (keyboardMarkup != null) {
            sendPhoto.setReplyMarkup( keyboardMarkup );
        }

        try {
            // Send the photo with caption
            bot.execute( sendPhoto );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
