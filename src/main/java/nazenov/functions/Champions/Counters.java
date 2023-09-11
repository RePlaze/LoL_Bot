package nazenov.functions.Champions;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class Counters {
    private static TelegramLongPollingBot bot = null;

    public Counters(TelegramLongPollingBot bot) {
        Counters.bot = bot;
    }

    public static void counters(String chatId) {

    }
}
