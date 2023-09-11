package nazenov.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class Back {
    private static TelegramLongPollingBot bot = null;

    public Back(TelegramLongPollingBot bot) {
        Back.bot = bot;
    }

    //move back
    public void backButton(String chatId) {

    }
}
