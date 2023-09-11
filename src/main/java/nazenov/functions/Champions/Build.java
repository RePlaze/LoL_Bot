package nazenov.functions.Champions;

import nazenov.utils.TelegramBotUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;

public class Build {
    private final TelegramLongPollingBot botInstance;

    public Build(TelegramLongPollingBot bot) {
        this.botInstance = bot; // Initialize botInstance here
    }

    public void build(String chatId, String championName) {
        // Create the URL for the champion
        String championUrl = "https://www.op.gg/champions/" + championName;

        try {
            String messageText = "Build information for " + championName + ":";
            SendMessage message = new SendMessage( chatId, messageText );

            // Fetch the web page
            Document document = Jsoup.connect( championUrl ).get();

            // Find the table with caption "<caption> Builds</caption>"
            Elements tables = document.select( "table" );
            Element targetTable = null;
            for (Element table : tables) {
                Element caption = table.selectFirst( "caption" );
                if (caption != null && caption.text().contains( "Builds" )) {
                    targetTable = table;
                    break;
                }
            }

            if (targetTable != null) {
                // Create a StringBuilder to build the table text with images
                StringBuilder tableTextWithImages = new StringBuilder();

                // Iterate through the rows of the table
                Elements rows = targetTable.select( "tr" );
                for (Element row : rows) {
                    // Iterate through the cells of the row
                    Elements cells = row.select( "td" );
                    for (Element cell : cells) {
                        // Check if the cell contains an image (e.g., an item image)
                        Element img = cell.select( "img" ).first();
                        if (img != null) {
                            // Append the image URL to the StringBuilder
                            String imgUrl = img.attr( "src" );
                            tableTextWithImages.append( "![Image](" ).append( imgUrl ).append( ") " );
                        }

                        // Append the cell text to the StringBuilder
                        String cellText = cell.text();

                        // Extract the text before the '%' character
                        int percentIndex = cellText.indexOf( '%' );
                        if (percentIndex != -1) {
                            cellText = cellText.substring( 0, percentIndex );
                        }

                        tableTextWithImages.append( cellText ).append( " " );
                    }
                    // Add a line break after each row
                    tableTextWithImages.append( "\n" );
                }

                // Set the table text with images as the message text
                message.setText( tableTextWithImages.toString() );
                System.out.println( tableTextWithImages );

                // Execute the message
                botInstance.execute( message );
            } else {
                TelegramBotUtil.sendFormattedText( botInstance, chatId, "'Builds' not found for " + championName, false, null );
            }
        } catch (IOException e) {
            e.printStackTrace();
            TelegramBotUtil.sendFormattedText( botInstance, chatId, "An error occurred while fetching the data.", false, null );
        } catch (Exception e) {
            e.printStackTrace();
            TelegramBotUtil.sendFormattedText( botInstance, chatId, "Error", false, null );
        }
    }
}
