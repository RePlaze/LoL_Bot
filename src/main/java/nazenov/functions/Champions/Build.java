package nazenov.functions.Champions;

import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class Build {
    public static void handleBuildButtonPress(AbsSender sender, Long chatId, Message message) {
        // Implement your logic for the "Build" button press here
        // For example, you can send a response or perform any other actions
        String response = "You pressed the 'Build' button!";
        TelegramBotUtil.sendFormattedText( sender, chatId.toString(), response, false, null );

        // You can also access the original message if needed
        String originalMessageText = message.getText();

        // Additional logic based on the original message text can be added here
    }
}
