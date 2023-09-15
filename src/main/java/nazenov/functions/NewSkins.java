package nazenov.functions;

import nazenov.utils.ImageData;
import nazenov.utils.SendMessage;
import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

public class NewSkins {
    private static final String PATCH_VERSION = "13.18";
    String url =
            "https://www.leagueoflegends.com/en-pl/news/game-updates/patch-" + PATCH_VERSION.replace( ".", "-" ) + "-notes/";
    private final TelegramLongPollingBot bot;
    private final ThreadPoolExecutor executor;
    private final InputFile reusableInputFile = new InputFile();
    private final SendPhoto reusableSendPhoto = new SendPhoto();
    private final Set<String> uniqueDescriptions = new HashSet<>();

    public NewSkins(TelegramLongPollingBot bot) {
        this.bot = bot;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool( 10 );
    }

    public void sendNewSkins(String chatId) {
        CompletableFuture.runAsync( () -> {
            sendMarkdownMessage( chatId, "*New LoL Skins*``` released in " + PATCH_VERSION + " patch ```" );
            getImageDataFromWebpage().forEach( imageData -> {
                if (!uniqueDescriptions.contains( imageData.getDescription() )) {
                    uniqueDescriptions.add( imageData.getDescription() );
                    sendImage( chatId, imageData );
                }
            } );
        }, executor );
    }

    private void sendMarkdownMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage( bot, executor );
        sendMessage.sendReplyMessage( chatId, message );
    }

    private void sendImage(String chatId, ImageData imageData) {
        try (InputStream inputStream = new URL( imageData.getImageUrl() ).openStream()) {
            reusableInputFile.setMedia( inputStream, "image.jpg" );
            reusableSendPhoto.setChatId( chatId );
            reusableSendPhoto.setPhoto( reusableInputFile );

            if (!imageData.getDescription().isEmpty())
                reusableSendPhoto.setCaption( imageData.getDescription() );

            try {
                bot.execute( reusableSendPhoto );
            } catch (TelegramApiException e) {
                handleTelegramApiException( chatId, e );
            }
        } catch (IOException e) {
            handleIOException( chatId, e );
        }
    }

    private void handleTelegramApiException(String chatId, TelegramApiException e) {
        e.printStackTrace();
        sendErrorMessage( chatId );
    }

    private void handleIOException(String chatId, IOException e) {
        e.printStackTrace();
        sendErrorMessage( chatId );
    }

    private void sendErrorMessage(String chatId) {
        TelegramBotUtil.sendFormattedText( bot, chatId, "Error sending Photo", false, null );
    }

    private Stream<ImageData> getImageDataFromWebpage() {
        try {
            Document document = Jsoup.connect( url ).get();
            return document.select( "div.skin-box" ).stream()
                    .map( skinBox -> {
                        Element imageElement = skinBox.selectFirst( "img[src^='https://images.contentstack.io/v3/assets/']" );
                        Element descriptionElement = skinBox.selectFirst( "h4.skin-title" );
                        String imageUrl = (imageElement != null) ? imageElement.attr( "src" ) : "";
                        String description = (descriptionElement != null) ? descriptionElement.text() : "";
                        return new ImageData( imageUrl, description );
                    } );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}