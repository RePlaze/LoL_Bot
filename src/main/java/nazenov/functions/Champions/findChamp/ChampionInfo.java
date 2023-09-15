package nazenov.functions.Champions.findChamp;

import nazenov.utils.OptionBar;
import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ChampionInfo {
    private final TelegramLongPollingBot botInstance;
    private final ExecutorService executorService;

    public ChampionInfo(TelegramLongPollingBot bot) {
        this.botInstance = bot;
        this.executorService = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
    }

    public void champOptions(String chatId, String championName) {
        CompletableFuture.runAsync( () -> {
            try {
                sendEmoji( chatId, championName );
                String imageUrl = getChampionImage( championName );
                SendPhoto sendPhoto = new SendPhoto( chatId, new InputFile( imageUrl ) );
                sendPhoto.setCaption( "``` select option for``` *" + championName + "*" );
                sendPhoto.setParseMode( "Markdown" );
                sendPhoto.setReplyMarkup( ChampOption.championOptionsKeyboard( chatId, championName ) );

                botInstance.execute( sendPhoto );
            } catch (TelegramApiException e) {
                handleTelegramApiException( e, chatId );
            }
        }, executorService );
    }

    public void selectChampion(String chatId) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "*Name of Champion?*", true, ChampionKeyboad.championButtons() );
    }

    private String getChampionImage(String championName) {
        return String.format( "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_0.jpg", championName.replaceAll( "'", "" ) );
    }

    public void handleUserInput(String chatId, String messageText) {
        String championName = messageText.substring( 0, 1 ).toUpperCase() + messageText.substring( 1 );
        SearchName searchName = new SearchName();
        String suggestion = searchName.findBestMatch( championName );

        if (!suggestion.equalsIgnoreCase( championName ))
            TelegramBotUtil.sendFormattedText( botInstance, chatId,
                    "*" + championName + "* not found. Did you mean *" + suggestion + "*?", true,
                    ChampionKeyboad.yesButton( suggestion ) );
        else
            champOptions( chatId, championName );
    }

    public void handleTelegramApiException(TelegramApiException e, String chatId) {
        e.printStackTrace();
        TelegramBotUtil.sendFormattedText( botInstance, chatId, "Error sending the photo.", false, null );
    }

    void sendEmoji(String chatId, String championName) {
        Map<String, String> championToEmojiMap = new HashMap<>();

        championToEmojiMap.put( "Aatrox", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Ahri", "\uD83D\uDC31" );
        championToEmojiMap.put( "Akali", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Alistar", "\uD83D\uDC04" );
        championToEmojiMap.put( "Amumu", "\uD83D\uDE22" );
        championToEmojiMap.put( "Anivia", "\uD83D\uDC26" );
        championToEmojiMap.put( "Annie", "\uD83D\uDD25" );
        championToEmojiMap.put( "Aphelios", "\uD83C\uDF19" );
        championToEmojiMap.put( "Ashe", "\uD83C\uDFF9" );
        championToEmojiMap.put( "AurelionSol", "\uD83C\uDF1F" );
        championToEmojiMap.put( "Azir", "\uD83C\uDFF0" );
        championToEmojiMap.put( "Bard", "\uD83C\uDFB5" );
        championToEmojiMap.put( "Blitzcrank", "\uD83E\uDD16" );
        championToEmojiMap.put( "Brand", "\uD83D\uDD25" );
        championToEmojiMap.put( "Braum", "\uD83D\uDEE1" );
        championToEmojiMap.put( "Caitlyn", "\uD83D\uDD2B" );
        championToEmojiMap.put( "Camille", "\uD83D\uDEE0" );
        championToEmojiMap.put( "Cassiopeia", "\uD83D\uDC0D" );
        championToEmojiMap.put( "ChoGath", "\uD83E\uDD96" );
        championToEmojiMap.put( "Corki", "\u2708️" );
        championToEmojiMap.put( "Darius", "\u2694️" );
        championToEmojiMap.put( "Diana", "\uD83C\uDF19" );
        championToEmojiMap.put( "Dr.Mundo", "\uD83D\uDC89" );
        championToEmojiMap.put( "Draven", "\uD83E\uDD91" );
        championToEmojiMap.put( "Ekko", "\u231B" );
        championToEmojiMap.put( "Elise", "\uD83D\uDD77" );
        championToEmojiMap.put( "Evelynn", "\uD83D\uDC79" );
        championToEmojiMap.put( "Ezreal", "\uD83C\uDF1F" );
        championToEmojiMap.put( "Fiddlesticks", "\uD83E\uDD16" );
        championToEmojiMap.put( "Fiora", "\uD83E\uDD8A" );
        championToEmojiMap.put( "Fizz", "\uD83D\uDC20" );
        championToEmojiMap.put( "Galio", "\uD83D\uDDFF" );
        championToEmojiMap.put( "Gangplank", "\u2620️" );
        championToEmojiMap.put( "Garen", "\u2694️" );
        championToEmojiMap.put( "Gnar", "\uD83D\uDC3B" );
        championToEmojiMap.put( "Gragas", "\uD83C\uDF7A" );
        championToEmojiMap.put( "Graves", "\uD83D\uDD2B" );
        championToEmojiMap.put( "Gwen", "\uD83E\uDD80" );
        championToEmojiMap.put( "Hecarim", "\uD83D\uDC0E" );
        championToEmojiMap.put( "Heimerdinger", "\uD83D\uDD27" );
        championToEmojiMap.put( "Illaoi", "\uD83E\uDD91" );
        championToEmojiMap.put( "Irelia", "\uD83E\uDD8A" );
        championToEmojiMap.put( "Ivern", "\uD83C\uDF33" );
        championToEmojiMap.put( "Janna", "\uD83C\uDF2A" );
        championToEmojiMap.put( "JarvanIV", "\uD83C\uDFD0" );
        championToEmojiMap.put( "Jax", "\u2694️" );
        championToEmojiMap.put( "Jayce", "\uD83D\uDD27" );
        championToEmojiMap.put( "Jhin", "\uD83D\uDD2B" );
        championToEmojiMap.put( "Jinx", "\uD83E\uDD28" );
        championToEmojiMap.put( "Kalista", "\uD83D\uDD2A" );
        championToEmojiMap.put( "Karma", "\u262F️" );
        championToEmojiMap.put( "Karthus", "\uD83D\uDC80" );
        championToEmojiMap.put( "Kassadin", "\uD83C\uDF0C" );
        championToEmojiMap.put( "Katarina", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Kayle", "\uD83D\uDC7C" );
        championToEmojiMap.put( "Kayn", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Kennen", "\u26A1" );
        championToEmojiMap.put( "KhaZix", "\uD83E\uDD97" );
        championToEmojiMap.put( "Kindred", "\uD83D\uDC3A\uD83D\uDC0F" );
        championToEmojiMap.put( "Kled", "\uD83D\uDC0E" );
        championToEmojiMap.put( "KogMaw", "\uD83D\uDC79" );
        championToEmojiMap.put( "LeBlanc", "\uD83C\uDFA3" );
        championToEmojiMap.put( "LeeSin", "\uD83E\uDD74" );
        championToEmojiMap.put( "Leona", "\u2600" );
        championToEmojiMap.put( "Lillia", "\uD83E\uDD8C" );
        championToEmojiMap.put( "Lissandra", "\u2744️" );
        championToEmojiMap.put( "Lucian", "\uD83D\uDD2B" );
        championToEmojiMap.put( "Lulu", "\uD83C\uDF3F" );
        championToEmojiMap.put( "Lux", "\uD83C\uDF1F" );
        championToEmojiMap.put( "Malphite", "\uD83D\uDDFF" );
        championToEmojiMap.put( "Malzahar", "\uD83D\uDC32" );
        championToEmojiMap.put( "Maokai", "\uD83C\uDF32" );
        championToEmojiMap.put( "MasterYi", "\u2694️" );
        championToEmojiMap.put( "MissFortune", "\uD83D\uDCB0" );
        championToEmojiMap.put( "Mordekaiser", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Morgana", "\uD83D\uDD2E" );
        championToEmojiMap.put( "Nami", "\uD83C\uDF0A" );
        championToEmojiMap.put( "Nasus", "\uD83D\uDC3A" );
        championToEmojiMap.put( "Nautilus", "\u2693" );
        championToEmojiMap.put( "Neeko", "\uD83C\uDF3A" );
        championToEmojiMap.put( "Nidalee", "\uD83D\uDC3E" );
        championToEmojiMap.put( "Nocturne", "\uD83C\uDF19" );
        championToEmojiMap.put( "Nunu&Willump", "\uD83E\uDDCA" );
        championToEmojiMap.put( "Olaf", "\u2694️" );
        championToEmojiMap.put( "Orianna", "\u2699️" );
        championToEmojiMap.put( "Ornn", "\uD83D\uDD25" );
        championToEmojiMap.put( "Pantheon", "\uD83D\uDE91" );
        championToEmojiMap.put( "Poppy", "\uD83D\uDD28" );
        championToEmojiMap.put( "Pyke", "\uD83D\uDDEF" );
        championToEmojiMap.put( "Qiyana", "\uD83C\uDF0A" );
        championToEmojiMap.put( "Quinn", "\uD83E\uDD85" );
        championToEmojiMap.put( "Rakan", "\uD83E\uDD8A" );
        championToEmojiMap.put( "Rammus", "\uD83D\uDC22" );
        championToEmojiMap.put( "RekSai", "\uD83E\uDD83" );
        championToEmojiMap.put( "Rell", "\uD83D\uDC0E" );
        championToEmojiMap.put( "Renekton", "\uD83D\uDC0E" );
        championToEmojiMap.put( "Rengar", "\uD83E\uDD81" );
        championToEmojiMap.put( "Riven", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Rumble", "\uD83D\uDD28" );
        championToEmojiMap.put( "Ryze", "\uD83D\uDCDC" );
        championToEmojiMap.put( "Samira", "\uD83D\uDD2B" );
        championToEmojiMap.put( "Sejuani", "\uD83D\uDC16" );
        championToEmojiMap.put( "Senna", "\uD83C\uDFB9" );
        championToEmojiMap.put( "Seraphine", "\uD83C\uDFB5" );
        championToEmojiMap.put( "Sett", "\uD83E\uDD4A" );
        championToEmojiMap.put( "Shaco", "\uD83E\uDD21" );
        championToEmojiMap.put( "Shen", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Shyvana", "\uD83D\uDC09" );
        championToEmojiMap.put( "Singed", "\uD83E\uDD79" );
        championToEmojiMap.put( "Sion", "\uD83D\uDC80" );
        championToEmojiMap.put( "Sivir", "\uD83C\uDFF9" );
        championToEmojiMap.put( "Skarner", "\uD83E\uDD83" );
        championToEmojiMap.put( "Sona", "\uD83C\uDFB5" );
        championToEmojiMap.put( "Soraka", "\uD83C\uDF1F" );
        championToEmojiMap.put( "Swain", "\uD83E\uDDA2" );
        championToEmojiMap.put( "Sylas", "\uD83E\uDD11" );
        championToEmojiMap.put( "Syndra", "\uD83C\uDF0C" );
        championToEmojiMap.put( "TahmKench", "\uD83D\uDC41" );
        championToEmojiMap.put( "Taliyah", "\uD83C\uDF2A" );
        championToEmojiMap.put( "Talon", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Taric", "\uD83D\uDC8E" );
        championToEmojiMap.put( "Teemo", "\uD83C\uDF44" );
        championToEmojiMap.put( "Thresh", "\uD83D\uDE9D" );
        championToEmojiMap.put( "Tristana", "\uD83D\uDE80" );
        championToEmojiMap.put( "Trundle", "\u2744️" );
        championToEmojiMap.put( "Tryndamere", "\u2694️" );
        championToEmojiMap.put( "TwistedFate", "\uD83C\uDFB4" );
        championToEmojiMap.put( "Twitch", "\uD83D\uDC00" );
        championToEmojiMap.put( "Udyr", "\uD83D\uDC3B" );
        championToEmojiMap.put( "Urgot", "\uD83E\uDD80" );
        championToEmojiMap.put( "Varus", "\uD83C\uDFF9" );
        championToEmojiMap.put( "Vayne", "\uD83E\uDD87" );
        championToEmojiMap.put( "Veigar", "\uD83E\uDD9B" );
        championToEmojiMap.put( "VelKoz", "\uD83D\uDC41" );
        championToEmojiMap.put( "Vi", "\uD83E\uDD1C\uD83E\uDD1C" );
        championToEmojiMap.put( "Viego", "\uD83E\uDD87" );
        championToEmojiMap.put( "Viktor", "\uD83D\uDD27" );
        championToEmojiMap.put( "Vladimir", "\uD83E\uDD87" );
        championToEmojiMap.put( "Volibear", "\uD83D\uDC3B" );
        championToEmojiMap.put( "Warwick", "\uD83E\uDD8A" );
        championToEmojiMap.put( "Wukong", "\uD83D\uDC12" );
        championToEmojiMap.put( "Xayah", "\uD83D\uDC26" );
        championToEmojiMap.put( "Xerath", "\u26A1" );
        championToEmojiMap.put( "XinZhao", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Yasuo", "\uD83C\uDF2A" );
        championToEmojiMap.put( "Yone", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Yorick", "\uD83D\uDC80" );
        championToEmojiMap.put( "Yuumi", "\uD83E\uDD3E" );
        championToEmojiMap.put( "Zac", "\uD83D\uDF7A" );
        championToEmojiMap.put( "Zed", "\uD83D\uDDE1" );
        championToEmojiMap.put( "Zeri", "\uD83E\uDD85" );
        championToEmojiMap.put( "Ziggs", "\uD83D\uDCA3" );
        championToEmojiMap.put( "Zilean", "\u231B" );
        championToEmojiMap.put( "Zoe", "\uD83C\uDF1F" );
        championToEmojiMap.put( "Zyra", "\uD83C\uDF31" );
        if (championName.equals( "Kaisa" ))
            TelegramBotUtil.sendFormattedText( botInstance, chatId, "\uD83D\uDC7E", true,
                    OptionBar.buildKeyboard() );
    }
}
