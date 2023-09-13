package nazenov.functions;

import nazenov.utils.ImageData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class News {
    private final TelegramLongPollingBot bot;

    public News(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    boolean newChampion = true;
    String championName = "Briar";
    boolean anons = true;
    String newChampionUrl = "https://www.leagueoflegends.com/en-us/event/briar-abilities-rundown/";

    public void checkNews(String chatId) {
        if (newChampion)
            newChampion( chatId, newChampionUrl, championName );
        if (anons)
            newsAnons( chatId );
    }

    private void newsAnons(String chatId) {
        patches( chatId );
    }

    public void newChampion(String chatId, String newChampionUrl, String championName) {
        try {
            Document document = Jsoup.connect( newChampionUrl ).get();

            Elements imgElements = document.select( "body img" );
            if (!imgElements.isEmpty()) {
                Element firstImage = imgElements.first();
                String imageUrl = Objects.requireNonNull( firstImage ).absUrl( "src" );

                ImageData imageData = new ImageData( imageUrl, "*News! New champion release Meet: " + championName + "*" );

                sendImage( chatId, imageData );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendImage(String chatId, ImageData imageData) {
        try {
            InputStream inputStream = new URL( imageData.getImageUrl() ).openStream();
            InputFile image = new InputFile( inputStream, "image.jpg" );
            SendPhoto sendPhoto = new SendPhoto( chatId, image );

            if (!imageData.getDescription().isEmpty()) {
                sendPhoto.setCaption( imageData.getDescription() );
                sendPhoto.setParseMode( "Markdown" );
            }

            bot.execute( sendPhoto );
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void patches(String chatId) {
        try {
            String todayPatch = "13-18";
            String webpageUrl = "https://www.leagueoflegends.com/en-us/news/game-updates/patch-" + todayPatch + "-notes/";
            Document document = Jsoup.connect( webpageUrl ).get();
            Element patchNotesContainer = document.select( "div#patch-notes-container" ).first();

            if (patchNotesContainer != null) {
                Elements images = patchNotesContainer.select( "img" );
                if (!images.isEmpty()) {
                    Element secondImage = images.get( 1 );
                    String imageUrl = secondImage.absUrl( "src" );

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId( chatId );
                    sendPhoto.setPhoto( new InputFile( imageUrl ) );
                    sendPhoto.setCaption( "*New Patch! check the updates*" );
                    sendPhoto.setParseMode( "Markdown" );

                    bot.execute( sendPhoto );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
