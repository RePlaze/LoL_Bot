package nazenov.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ButtonPressHandler {

    public static void handleBuildButtonPress(AbsSender sender, Long chatId, Message message) {
        // Implement your logic for the "Build" button press here
        // For example, you can send a response or perform any other actions
        String response = "You pressed the 'Build' button!";
        SendMessage sendMessage = new SendMessage( chatId.toString(), response );
        try {
            sender.execute( sendMessage );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
