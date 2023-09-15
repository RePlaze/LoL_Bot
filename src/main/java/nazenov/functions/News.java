package nazenov.functions;

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

    private void sendImage(String chatId, String imageUrl, String caption) {
        try {
            InputStream inputStream = new URL( imageUrl ).openStream();
            InputFile image = new InputFile( inputStream, "image.jpg" );
            SendPhoto sendPhoto = new SendPhoto( chatId, image );

            if (!caption.isEmpty()) {
                sendPhoto.setCaption( caption );
                sendPhoto.setParseMode( "Markdown" );
            }

            bot.execute( sendPhoto );
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void checkNews(String chatId) {
        checkNewChampion( chatId );
        checkNewsAnons( chatId );
    }

    private void checkNewChampion(String chatId) {
        String championName = "Briar";
        String newChampionUrl = "https://www.leagueoflegends.com/en-us/event/briar-abilities-rundown/";

        try {
            Document document = Jsoup.connect( newChampionUrl ).get();
            Elements imgElements = document.select( "body img" );

            if (!imgElements.isEmpty()) {
                Element firstImage = imgElements.first();
                String imageUrl = Objects.requireNonNull( firstImage ).absUrl( "src" );

                String caption = "*News! New champion release Meet: " + championName + "*";
                sendImage( chatId, imageUrl, caption );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkNewsAnons(String chatId) {
        checkPatches( chatId );
    }

    private void checkPatches(String chatId) {
        try {
            String todayPatch = "13-18";
            String webpageUrl = "https://www.leagueoflegends.com/en-us/news/game-updates/patch-" + todayPatch + "-notes/";
            Document document = Jsoup.connect( webpageUrl ).get();
            Element patchNotesContainer = document.select( "div#patch-notes-container" ).first();

            if (patchNotesContainer != null) {
                Elements images = patchNotesContainer.select( "img" );

                if (!images.isEmpty() && images.size() >= 2) {
                    Element secondImage = images.get( 1 );
                    String imageUrl = secondImage.absUrl( "src" );

                    String caption = "*New Patch! check the updates*";
                    sendImage( chatId, imageUrl, caption );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
