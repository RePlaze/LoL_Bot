package nazenov.functions.Champions;

import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Build {
    private final TelegramLongPollingBot botInstance;
    private final ThreadPoolExecutor executor;

    public Build(TelegramLongPollingBot bot) {
        this.botInstance = bot;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool( 5 ); // Use a thread pool for parallel processing
    }

    public void build(String chatId, String championName) {
        // Create the URL for the champion
        String championUrl = "https://www.op.gg/champions/" + championName;

        CompletableFuture.runAsync( () -> { // Use CompletableFuture for async processing
            try {
                Document document = Jsoup.connect( championUrl ).get();

                // Find all images within the <main> section
                Element mainSection = document.selectFirst( "main" );
                assert mainSection != null;
                Elements images = mainSection.select( "img" );

                if (!images.isEmpty()) {
                    // Reverse the order of images
                    List<Element> reversedImages = new ArrayList<>( images );
                    Collections.reverse( reversedImages );

                    int maxImagesPerResponse = 9;
                    int maxResponses = 2;
                    int responseCount = 0;
                    int startIndex = 0;

                    while (responseCount < maxResponses && startIndex < reversedImages.size()) {
                        int endIndex = Math.min( startIndex + maxImagesPerResponse, reversedImages.size() );
                        List<Element> subList = reversedImages.subList( startIndex, endIndex );

                        // Create a list to store InputMediaPhoto objects for the album
                        List<InputMediaPhoto> album = new ArrayList<>();

                        // Add the images from the sublist to the album
                        for (Element image : subList) {
                            String imageUrl = image.attr( "src" );
                            if (!imageUrl.isEmpty()) {
                                InputMediaPhoto mediaPhoto = new InputMediaPhoto();
                                mediaPhoto.setMedia( imageUrl );
                                album.add( mediaPhoto );
                            }
                        }

                        // Send the album as a media group, but skip the first response
                        if (!album.isEmpty() && responseCount > 0) {
                            sendMediaGroup( chatId, album );
                        }

                        responseCount++;
                        startIndex += maxImagesPerResponse;
                    }

                    // Send a reply message
                    String formatName = "*" + championName + "*";
                    String replyMessage = "Build for " + formatName + "\n1. Boots option\n2. *Build #1: 1 item -> 2 " +
                            "item -> 3 item*\n3. `Build #2: (Alternative)`";
                    sendReplyMessage( chatId, replyMessage );
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleException( chatId );
            }
        }, executor );
    }

    public void sendMediaGroup(String chatId, List<InputMediaPhoto> mediaList) {
        CompletableFuture.runAsync( () -> { // Use CompletableFuture for async processing
            try {
                SendMediaGroup mediaGroup = new SendMediaGroup();
                mediaGroup.setChatId( chatId );
                Collections.reverse( mediaList );
                mediaGroup.setMedias( Collections.unmodifiableList( mediaList ) );

                botInstance.execute( mediaGroup );
            } catch (Exception e) {
                e.printStackTrace();
                handleException( chatId );
            }
        }, executor );
    }
    public void sendReplyMessage(String chatId, String message) {
        CompletableFuture.runAsync( () -> { // Use CompletableFuture for async processing
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId( chatId );
            sendMessage.setText( message );
            sendMessage.enableMarkdown( true );

            try {
                System.out.println( "Hello World!" );
                botInstance.execute( sendMessage );
            } catch (TelegramApiException e) {
                e.printStackTrace();
                handleException( chatId );
            }
        }, executor );
    }

    private void handleException(String chatId) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "An error occurred.", false, null );
    }
}
