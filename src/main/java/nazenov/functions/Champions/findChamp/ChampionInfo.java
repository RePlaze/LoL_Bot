package nazenov.functions.Champions.findChamp;

import nazenov.utils.OptionBar;
import nazenov.utils.TelegramBotUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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

    public void champOptions(String chatId, String champion) {
        System.out.println( champion );
        SearchName sn = new SearchName();
        String championName = sn.findBestMatch( champion );
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
        return String.format( "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_0.jpg",
                championName.replaceAll( "'", "" ) );
    }

    public void handleUserInput(String chatId, String championName) {
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
        switch (championName) {
            case "Aatrox":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Ahri":
                send( chatId, "\uD83D\uDC31" );
                break;
            case "Akali":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Alistar":
                send( chatId, "\uD83D\uDC04" );
                break;
            case "Amumu":
                send( chatId, "\uD83D\uDE22" );
                break;
            case "Anivia":
                send( chatId, "\uD83D\uDC26" );
                break;
            case "Annie":
                send( chatId, "\uD83D\uDD25" );
                break;
            case "Aphelios":
                send( chatId, "\uD83C\uDF19" );
                break;
            case "Ashe":
                send( chatId, "\uD83C\uDFF9" );
                break;
            case "AurelionSol":
                send( chatId, "\uD83C\uDF1F" );
                break;
            case "Azir":
                send( chatId, "\uD83C\uDFF0" );
                break;
            case "Bard":
                send( chatId, "\uD83C\uDFB5" );
                break;
            case "Blitzcrank":
                send( chatId, "\uD83E\uDD16" );
                break;
            case "Brand":
                send( chatId, "\uD83D\uDD25" );
                break;
            case "Braum":
                send( chatId, "\uD83D\uDEE1" );
                break;
            case "Caitlyn":
                send( chatId, "\uD83D\uDD2B" );
                break;
            case "Camille":
                send( chatId, "\uD83D\uDEE0" );
                break;
            case "Cassiopeia":
                send( chatId, "\uD83D\uDC0D" );
                break;
            case "ChoGath":
                send( chatId, "\uD83E\uDD96" );
                break;
            case "Corki":
                send( chatId, "\u2708️" );
                break;
            case "Darius":
                send( chatId, "\u2694️" );
                break;
            case "Diana":
                send( chatId, "\uD83C\uDF19" );
                break;
            case "DrMundo":
                send( chatId, "\uD83D\uDC89" );
                break;
            case "Draven":
                send( chatId, "\uD83E\uDD91" );
                break;
            case "Ekko":
                send( chatId, "\u231B" );
                break;
            case "Elise":
                send( chatId, "\uD83D\uDD77" );
                break;
            case "Evelynn":
                send( chatId, "\uD83D\uDC79" );
                break;
            case "Ezreal":
                send( chatId, "\uD83C\uDF1F" );
                break;
            case "Fiddlesticks":
                send( chatId, "\uD83E\uDD16" );
                break;
            case "Fiora":
                send( chatId, "\uD83E\uDD8A" );
                break;
            case "Fizz":
                send( chatId, "\uD83D\uDC20" );
                break;
            case "Galio":
                send( chatId, "\uD83D\uDDFF" );
                break;
            case "Gangplank":
                send( chatId, "\u2620️" );
                break;
            case "Garen":
                send( chatId, "\u2694️" );
                break;
            case "Gnar":
                send( chatId, "\uD83D\uDC3B" );
                break;
            case "Gragas":
                send( chatId, "\uD83C\uDF7A" );
                break;
            case "Graves":
                send( chatId, "\uD83D\uDD2B" );
                break;
            case "Gwen":
                send( chatId, "\uD83E\uDD80" );
                break;
            case "Hecarim":
                send( chatId, "\uD83D\uDC0E" );
                break;
            case "Heimerdinger":
                send( chatId, "\uD83D\uDD27" );
                break;
            case "Illaoi":
                send( chatId, "\uD83E\uDD91" );
                break;
            case "Irelia":
                send( chatId, "\uD83E\uDD8A" );
                break;
            case "Ivern":
                send( chatId, "\uD83C\uDF33" );
                break;
            case "Janna":
                send( chatId, "\uD83C\uDF2A" );
                break;
            case "JarvanIV":
                send( chatId, "\uD83C\uDFD0" );
                break;
            case "Jax":
                send( chatId, "\u2694️" );
                break;
            case "Jayce":
                send( chatId, "\uD83D\uDD27" );
                break;
            case "Jhin":
                send( chatId, "\uD83D\uDD2B" );
                break;
            case "Jinx":
                send( chatId, "\uD83E\uDD28" );
                break;
            case "Kalista":
                send( chatId, "\uD83D\uDD2A" );
                break;
            case "Karma":
                send( chatId, "\u262F️" );
                break;
            case "Karthus":
                send( chatId, "\uD83D\uDC80" );
                break;
            case "Kassadin":
                send( chatId, "\uD83C\uDF0C" );
                break;
            case "Katarina":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Kayle":
                send( chatId, "\uD83D\uDC7C" );
                break;
            case "Kayn":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Kennen":
                send( chatId, "\u26A1" );
                break;
            case "KhaZix":
                send( chatId, "\uD83E\uDD97" );
                break;
            case "Kindred":
                send( chatId, "\uD83D\uDC3A\uD83D\uDC0F" );
                break;
            case "Kled":
                send( chatId, "\uD83D\uDC0E" );
                break;
            case "KogMaw":
                send( chatId, "\uD83D\uDC79" );
                break;
            case "LeBlanc":
                send( chatId, "\uD83C\uDFA3" );
                break;
            case "LeeSin":
                send( chatId, "\uD83E\uDD74" );
                break;
            case "Leona":
                send( chatId, "\u2600" );
                break;
            case "Lillia":
                send( chatId, "\uD83E\uDD8C" );
                break;
            case "Lissandra":
                send( chatId, "\u2744️" );
                break;
            case "Lucian":
                send( chatId, "\uD83D\uDD2B" );
                break;
            case "Lulu":
                send( chatId, "\uD83C\uDF3F" );
                break;
            case "Lux":
                send( chatId, "\uD83C\uDF1F" );
                break;
            case "Malphite":
                send( chatId, "\uD83D\uDDFF" );
                break;
            case "Malzahar":
                send( chatId, "\uD83D\uDC32" );
                break;
            case "Maokai":
                send( chatId, "\uD83C\uDF32" );
                break;
            case "MasterYi":
                send( chatId, "\u2694️" );
                break;
            case "MissFortune":
                send( chatId, "\uD83D\uDCB0" );
                break;
            case "Mordekaiser":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Morgana":
                send( chatId, "\uD83D\uDD2E" );
                break;
            case "Nami":
                send( chatId, "\uD83C\uDF0A" );
                break;
            case "Nasus":
                send( chatId, "\uD83D\uDC3A" );
                break;
            case "Nautilus":
                send( chatId, "\u2693" );
                break;
            case "Neeko":
                send( chatId, "\uD83C\uDF3A" );
                break;
            case "Nidalee":
                send( chatId, "\uD83D\uDC3E" );
                break;
            case "Nocturne":
                send( chatId, "\uD83C\uDF19" );
                break;
            case "Nunu&Willump":
                send( chatId, "\uD83E\uDDCA" );
                break;
            case "Olaf":
                send( chatId, "\u2694️" );
                break;
            case "Orianna":
                send( chatId, "\u2699️" );
                break;
            case "Ornn":
                send( chatId, "\uD83D\uDD25" );
                break;
            case "Pantheon":
                send( chatId, "\uD83D\uDE91" );
                break;
            case "Poppy":
                send( chatId, "\uD83D\uDD28" );
                break;
            case "Pyke":
                send( chatId, "\uD83D\uDDEF" );
                break;
            case "Qiyana":
                send( chatId, "\uD83C\uDF0A" );
                break;
            case "Quinn":
                send( chatId, "\uD83E\uDD85" );
                break;
            case "Rakan":
                send( chatId, "\uD83E\uDD8A" );
                break;
            case "Rammus":
                send( chatId, "\uD83D\uDC22" );
                break;
            case "RekSai":
                send( chatId, "\uD83E\uDD83" );
                break;
            case "Rell":
                send( chatId, "\uD83D\uDC0E" );
                break;
            case "Renekton":
                send( chatId, "\uD83D\uDC0E" );
                break;
            case "Rengar":
                send( chatId, "\uD83E\uDD81" );
                break;
            case "Riven":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Rumble":
                send( chatId, "\uD83D\uDD28" );
                break;
            case "Ryze":
                send( chatId, "\uD83D\uDCDC" );
                break;
            case "Samira":
                send( chatId, "\uD83D\uDD2B" );
                break;
            case "Sejuani":
                send( chatId, "\uD83D\uDC16" );
                break;
            case "Senna":
                send( chatId, "\uD83C\uDFB9" );
                break;
            case "Seraphine":
                send( chatId, "\uD83E\uDD4A" );
                break;
            case "Sett":
                send( chatId, "\uD83E\uDD4A" );
                break;
            case "Shaco":
                send( chatId, "\uD83E\uDD21" );
                break;
            case "Shen":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Shyvana":
                send( chatId, "\uD83D\uDC09" );
                break;
            case "Singed":
                send( chatId, "\uD83E\uDD79" );
                break;
            case "Sion":
                send( chatId, "\uD83D\uDE9E" );
                break;
            case "Sivir":
                send( chatId, "\uD83E\uDD85" );
                break;
            case "Skarner":
                send( chatId, "\uD83D\uDD28" );
                break;
            case "Sona":
                send( chatId, "\uD83C\uDFB5" );
                break;
            case "Soraka":
                send( chatId, "\uD83E\uDD8C" );
                break;
            case "Swain":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Sylas":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Syndra":
                send( chatId, "\uD83E\uDDDD" );
                break;
            case "TahmKench":
                send( chatId, "\uD83D\uDC1F" );
                break;
            case "Taliyah":
                send( chatId, "\uD83C\uDF0C" );
                break;
            case "Talon":
                send( chatId, "\uD83D\uDD25" );
                break;
            case "Taric":
                send( chatId, "\uD83D\uDC9A" );
                break;
            case "Teemo":
                send( chatId, "\uD83C\uDF3F" );
                break;
            case "Thresh":
                send( chatId, "\u2694️" );
                break;
            case "Tristana":
                send( chatId, "\uD83C\uDFC3" );
                break;
            case "Trundle":
                send( chatId, "\uD83E\uDD91" );
                break;
            case "Tryndamere":
                send( chatId, "\uD83D\uDEE1" );
                break;
            case "TwistedFate":
                send( chatId, "\uD83C\uDFB2" );
                break;
            case "Twitch":
                send( chatId, "\uD83D\uDD2E" );
                break;
            case "Udyr":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Ksante":
                send( chatId, "\uD83D\uDC3B" );
                break;
            case "Urgot":
                send( chatId, "\uD83E\uDD8E" );
                break;
            case "Varus":
                send( chatId, "\uD83D\uDCB0" );
                break;
            case "Vayne":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Veigar":
                send( chatId, "\uD83E\uDDDD" );
                break;
            case "VelKoz":
                send( chatId, "\uD83E\uDD16" );
                break;
            case "Vi":
                send( chatId, "\uD83E\uDD4A" );
                break;
            case "Viego":
                send( chatId, "\uD83D\uDC80" );
                break;
            case "Viktor":
                send( chatId, "\uD83D\uDD25" );
                break;
            case "Vladimir":
                send( chatId, "\uD83E\uDE78" );
                break;
            case "Volibear":
                send( chatId, "\uD83D\uDC3B" );
                break;
            case "Warwick":
                send( chatId, "\uD83D\uDC3A" );
                break;
            case "Wukong":
                send( chatId, "\uD83E\uDD8A" );
                break;
            case "Xayah":
                send( chatId, "\uD83C\uDFB9" );
                break;
            case "Xerath":
                send( chatId, "\uD83C\uDF19" );
                break;
            case "XinZhao":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Yasuo":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Yone":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Yorick":
                send( chatId, "\uD83E\uDDDD" );
                break;
            case "Yuumi":
                send( chatId, "\uD83E\uDD8C" );
                break;
            case "Zac":
                send( chatId, "\uD83D\uDC32" );
                break;
            case "Zed":
                send( chatId, "\uD83D\uDDE1" );
                break;
            case "Zeri":
                send( chatId, "\uD83E\uDD16" );
                break;
            case "Ziggs":
                send( chatId, "\uD83D\uDD27" );
                break;
            case "Zilean":
                send( chatId, "\u23F0" );
                break;
            case "Zoe":
                send( chatId, "\uD83D\uDDDD" );
                break;
            case "Zyra":
                send( chatId, "\uD83C\uDF3F" );
                break;
            case "Kaisa":
                send( chatId, "\uD83D\uDC7E" );
                break;
            case "Briar":
                send( chatId, "\uD83E\uDDDB" );
                break;
        }
    }

    public void send(String chatId, String text) {
        TelegramBotUtil.sendFormattedText( botInstance, chatId, text, true, OptionBar.buildKeyboard() );
    }

}
