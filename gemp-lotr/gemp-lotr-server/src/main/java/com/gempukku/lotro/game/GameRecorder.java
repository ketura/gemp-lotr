package com.gempukku.lotro.game;

import com.gempukku.lotro.common.ApplicationConfiguration;
import com.gempukku.lotro.game.state.EventSerializer;
import com.gempukku.lotro.game.state.GameCommunicationChannel;
import com.gempukku.lotro.game.state.GameEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class GameRecorder {
    private static String _possibleChars = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static int _charsCount = _possibleChars.length();

    private GameHistoryService _gameHistoryService;

    public GameRecorder(GameHistoryService gameHistoryService) {
        _gameHistoryService = gameHistoryService;
    }

    private String randomUid() {
        int length = 16;
        char[] chars = new char[length];
        Random rnd = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++)
            chars[i] = _possibleChars.charAt(rnd.nextInt(_charsCount));

        return new String(chars);
    }

    public InputStream getRecordedGame(String playerId, String gameId) throws IOException {
        final File file = getRecordingFile(playerId, gameId);
        if (!file.exists() || !file.isFile())
            return null;
        return new InflaterInputStream(new FileInputStream(file));
    }

    public GameRecordingInProgress recordGame(LotroGameMediator lotroGame, LotroFormat format, final String tournament, final Map<String, String> deckNames) {
        final Date startData = new Date();
        final Map<String, GameCommunicationChannel> recordingChannels = new HashMap<String, GameCommunicationChannel>();
        for (String playerId : lotroGame.getPlayersPlaying()) {
            GameCommunicationChannel recordChannel = new GameCommunicationChannel(playerId, 0);
            lotroGame.addGameStateListener(playerId, recordChannel);
            recordingChannels.put(playerId, recordChannel);
        }

        return (winner, winReason, loser, loseReason) -> {
            Map<String, String> playerRecordingId = saveRecordedChannels(recordingChannels);
            _gameHistoryService.addGameHistory(winner, loser, winReason, loseReason, playerRecordingId.get(winner), playerRecordingId.get(loser), format.getName(), tournament, deckNames.get(winner), deckNames.get(loser), startData, new Date());

            if(format.isPlaytest())
            {
                String url = "https://docs.google.com/forms/d/e/1FAIpQLSdKJrCmjoyUqDTusDcpNoWAmvkGdzQqTxWGpdNIFX9biCee-A/viewform?usp=pp_url&entry.1592109986=";
                String winnerURL = "https://play.lotrtcgpc.net/gemp-lotr/game.html%3FreplayId%3D" + winner + "$" + playerRecordingId.get(winner);
                String loserURL = "https://play.lotrtcgpc.net/gemp-lotr/game.html%3FreplayId%3D" + loser + "$" + playerRecordingId.get(loser);
                url += winnerURL + "%20" + loserURL;
                lotroGame.sendMessageToPlayers("Thank you for playtesting!  If you have any feedback, bugs, or other issues to report about this match, <a href= '" + url + "'>please do so using this form.</a>");
            }

        };
    }

    public interface GameRecordingInProgress {
        void finishRecording(String winner, String winReason, String loser, String loseReason);
    }

    private File getRecordingFile(String playerId, String gameId) {
        File gameReplayFolder = new File(ApplicationConfiguration.getProperty("application.root"), "replay");
        File playerReplayFolder = new File(gameReplayFolder, playerId);
        return new File(playerReplayFolder, gameId + ".xml.gz");
    }

    private OutputStream getRecordingWriteStream(String playerId, String gameId) throws IOException {
        File recordingFile = getRecordingFile(playerId, gameId);
        recordingFile.getParentFile().mkdirs();

        Deflater deflater = new Deflater(9);
        return new DeflaterOutputStream(new FileOutputStream(recordingFile), deflater);
    }

    private Map<String, String> saveRecordedChannels(Map<String, GameCommunicationChannel> gameProgress) {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, GameCommunicationChannel> playerRecordings : gameProgress.entrySet()) {
            String playerId = playerRecordings.getKey();

            String gameRecordingId = getRecordingId(playerId);
            final List<GameEvent> gameEvents = playerRecordings.getValue().consumeGameEvents();

            try {
                OutputStream replayStream = getRecordingWriteStream(playerId, gameRecordingId);
                try {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document doc = documentBuilder.newDocument();
                    Element gameReplay = doc.createElement("gameReplay");
                    EventSerializer serializer = new EventSerializer();
                    for (GameEvent gameEvent : gameEvents) {
                        gameReplay.appendChild(serializer.serializeEvent(doc, gameEvent));
                    }

                    doc.appendChild(gameReplay);

                    // Prepare the DOM document for writing
                    Source source = new DOMSource(doc);

                    // Prepare the output file
                    Result streamResult = new StreamResult(replayStream);

                    // Write the DOM document to the file
                    Transformer xformer = TransformerFactory.newInstance().newTransformer();
                    xformer.transform(source, streamResult);
                } finally {
                    replayStream.close();
                }
            } catch (Exception exp) {

            }
            result.put(playerId, gameRecordingId);
        }
        return result;
    }

    private String getRecordingId(String playerId) {
        String result;
        File recordingFile;
        do {
            result = randomUid();
            recordingFile = getRecordingFile(playerId, result);
        } while (recordingFile.exists());
        return result;
    }
}
