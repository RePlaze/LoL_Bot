package nazenov.functions;

import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatchDate {
    private final TelegramLongPollingBot bot;

    public PatchDate(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void patches(String chatId) {
        try {
            String webpageUrl = "https://earlygame.com/lol/2023-patch-schedule";
            Document document = Jsoup.connect(webpageUrl).get();
            Elements rows = document.select("table tbody tr");

            List<String> patchInfoList = new ArrayList<>();

            // Get today's date and day
            Calendar calendar = new GregorianCalendar();
            Date today = calendar.getTime();
            int todayDay = calendar.get(Calendar.DAY_OF_MONTH);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.ENGLISH);

            for (Element row : rows) {
                Elements columns = row.select("td");
                if (columns.size() >= 2) {
                    String patchName = columns.get(0).text();
                    String patchDateStr = columns.get(1).text();

                    // Check if patchDateStr matches the expected date format
                    if (isValidDate(patchDateStr, dateFormat)) {
                        try {
                            Date patchDate = dateFormat.parse(patchDateStr);
                            calendar.setTime(patchDate);
                            int patchDay = calendar.get(Calendar.DAY_OF_MONTH);

                            if (patchDate.after(today)) {
                                patchInfoList.add(patchName + " -> " + patchDateStr);
                            } else {
                                patchInfoList.add("[" + patchName + "](https://www.leagueoflegends" +
                                        ".com/en-au/news/game-updates/patch-" +
                                        patchName.replaceAll("\\.", "-")
                                                .replaceAll(" ", "") + "-notes) " +
                                        "-> " + patchDateStr);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        patchInfoList.add("[" + patchName + "](https://www.leagueoflegends" +
                                ".com/en-au/news/game-updates/patch-" +
                                patchName.replaceAll("\\.", "-")
                                        .replaceAll(" ", "") + "-notes) " +
                                "->! " + patchDateStr);
                    }
                }
            }

            // Create the Telegram response
            StringBuilder responseMessage = new StringBuilder("*Patch Dates:*\n");
            for (String patchInfo : patchInfoList) {
                String modifiedPatchInfo = patchInfo
                        .replaceAll("LoL", "")
                        .replaceAll("Patch", "")
                        .replaceAll("nesday", "")
                        .replaceAll("rsday", "")
                        .replaceAll("esday", "");
                responseMessage.append(modifiedPatchInfo).append("\n");
            }
            TelegramBotUtil.sendFormattedText(bot, chatId, responseMessage.toString(), true, null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isValidDate(String dateStr, DateFormat dateFormat) {
        dateFormat.setLenient(false); // Disable lenient parsing
        try {
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
