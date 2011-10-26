package com.gempukku.lotro.game;

import com.gempukku.lotro.db.GameHistoryDAO;
import com.gempukku.lotro.game.state.EventSerializer;
import com.gempukku.lotro.game.state.GameEvent;
import com.gempukku.lotro.game.state.GatheringParticipantCommunicationChannel;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GameRecorder {
    private static String _possibleChars = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static int _charsCount = _possibleChars.length();

    private GameHistoryDAO _gameHistoryDao;

    public GameRecorder(GameHistoryDAO gameHistoryDao) {
        _gameHistoryDao = gameHistoryDao;
    }

    private String randomUid() {
        int length = 16;
        char[] chars = new char[length];
        Random rnd = new Random();
        for (int i = 0; i < length; i++)
            chars[i] = _possibleChars.charAt(rnd.nextInt(_charsCount));

        return new String(chars);
    }

    public InputStream getRecordedGame(String playerId, String gameId) throws IOException {
        final File file = getRecordingFile(playerId, gameId);
        if (!file.exists() || !file.isFile())
            return null;
        return new FileInputStream(file);
    }

    public GameRecordingInProgress recordGame(LotroGameMediator lotroGame) {
        final Date startData = new Date();
        final Map<String, GatheringParticipantCommunicationChannel> recordingChannels = new HashMap<String, GatheringParticipantCommunicationChannel>();
        for (String playerId : lotroGame.getPlayersPlaying()) {
            GatheringParticipantCommunicationChannel recordChannel = new GatheringParticipantCommunicationChannel(playerId);
            lotroGame.addGameStateListener(playerId, recordChannel);
            recordingChannels.put(playerId, recordChannel);
        }

        return new GameRecordingInProgress() {
            @Override
            public void finishRecording(String winner, String winReason, String loser, String loseReason) {
                Map<String, String> playerRecordingId = saveRecordedChannels(recordingChannels);
                _gameHistoryDao.addGameHistory(winner, loser, winReason, loseReason, playerRecordingId.get(winner), playerRecordingId.get(loser), startData, new Date());
            }
        };
    }

    public interface GameRecordingInProgress {
        public void finishRecording(String winner, String winReason, String loser, String loseReason);
    }

    private File getRecordingFile(String playerId, String gameId) {
        File gameReplayFolder = new File("i:\\gemp-lotr\\replay");
        File playerReplayFolder = new File(gameReplayFolder, playerId);
        return new File(playerReplayFolder, gameId + ".xml");
    }

    private Map<String, String> saveRecordedChannels(Map<String, GatheringParticipantCommunicationChannel> gameProgress) {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, GatheringParticipantCommunicationChannel> playerRecordings : gameProgress.entrySet()) {
            String playerId = playerRecordings.getKey();

            File replayFile;
            String gameRecordingId;
            do {
                gameRecordingId = randomUid();
                replayFile = getRecordingFile(playerId, gameRecordingId);
            } while (replayFile.exists());

            replayFile.getParentFile().mkdirs();

            final List<GameEvent> gameEvents = playerRecordings.getValue().consumeGameEvents();

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
                Result streamResult = new StreamResult(replayFile);

                // Write the DOM document to the file
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, streamResult);
            } catch (Exception exp) {

            }
            result.put(playerId, gameRecordingId);
        }
        return result;
    }
}
