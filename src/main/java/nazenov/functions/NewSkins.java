package nazenov.functions;

import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import nazenov.utils.ImageData;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NewSkins {
    private static TelegramLongPollingBot bot = null;

    public NewSkins(TelegramLongPollingBot bot) {
        NewSkins.bot = bot;
    }

    public void sendNewSkins(String chatId) {
        CompletableFuture.runAsync(() -> {
            List<ImageData> imageDataList = getImageDataFromWebpageWithMinSize(480, 270);
            if (!imageDataList.isEmpty())
                for (ImageData imageData : imageDataList)
                    sendImage(chatId, imageData);
             else
                sendErrorMessage(chatId);
        });
    }

    private void sendImage(String chatId, ImageData imageData) {
        try {
            SendMessage headerMessage =
                    new SendMessage(chatId, "*New LoL Skins: All Skins Released in 2023*");
            headerMessage.enableMarkdown( true );
            bot.execute(headerMessage);
            InputStream inputStream = new URL(imageData.getImageUrl()).openStream();
            InputFile image = new InputFile(inputStream, "image.jpg");
            SendPhoto sendPhoto = new SendPhoto(chatId, image);

            if (!imageData.getDescription().isEmpty()) {
                sendPhoto.setCaption(imageData.getDescription());
            }

            bot.execute(sendPhoto);
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
            sendErrorMessage(chatId);
        }
    }

    private List<ImageData> getImageDataFromWebpageWithMinSize(int minWidth, int minHeight) {
        List<ImageData> imageDataList = new ArrayList<>();

        try {
            String webpageUrl = "https://earlygame.com/lol/new-upcoming-skins";
            Document document = Jsoup.connect(webpageUrl).get();
            Elements images = document.select("img[src^='https://prod.assets.earlygamecdn.com/images/']");

            for (Element image : images) {
                String imageUrl = image.attr("src");
                int width = Integer.parseInt(image.attr("width"));
                int height = Integer.parseInt(image.attr("height"));

                if (width >= minWidth && height >= minHeight) {
                    String description = getImageDescriptionFromElement(image);
                    imageDataList.add(new ImageData(imageUrl, description));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return imageDataList.stream().skip(Math.max(0, imageDataList.size() - 3)).toList();
    }

    private String getImageDescriptionFromElement(Element image) {
        String description = image.attr("alt");
        if (description.isEmpty()) description = "No description available";
        return description;
    }

    private static void sendErrorMessage(String chatId) {
        TelegramBotUtil.sendFormattedText(bot, chatId, "An error occurred. Please try again later.", false, null);

    }
}