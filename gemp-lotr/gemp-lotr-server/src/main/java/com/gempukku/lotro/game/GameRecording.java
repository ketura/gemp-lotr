package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.EventSerializer;
import com.gempukku.lotro.game.state.GameEvent;
import com.gempukku.lotro.game.state.GatheringParticipantCommunicationChannel;
import com.gempukku.lotro.logic.timing.GameResultListener;
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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameRecording implements GameResultListener {
    private Map<String, GatheringParticipantCommunicationChannel> _gameProgress;

    private static String _possibleChars = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static int _charsCount = _possibleChars.length();

    public GameRecording(Map<String, GatheringParticipantCommunicationChannel> gameProgress) {
        _gameProgress = gameProgress;
    }

    private String randomUid() {
        int length = 16;
        char[] chars = new char[length];
        Random rnd = new Random();
        for (int i = 0; i < length; i++)
            chars[i] = _possibleChars.charAt(rnd.nextInt(_charsCount));

        return new String(chars);
    }

    @Override
    public void gameFinished(String winnerPlayerId, Set<String> loserPlayerIds, String reason) {
        File gameReplayFolder = new File("i:\\gemp-lotr\\replay");
        gameReplayFolder.mkdirs();
        for (Map.Entry<String, GatheringParticipantCommunicationChannel> playerRecordings : _gameProgress.entrySet()) {
            String playerId = playerRecordings.getKey();
            File playerReplayFolder = new File(gameReplayFolder, playerId);
            playerReplayFolder.mkdir();
            File replayFile;
            do {
                replayFile = new File(playerReplayFolder, randomUid() + ".xml");
            } while (replayFile.exists());

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
                Result result = new StreamResult(replayFile);

                // Write the DOM document to the file
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);
            } catch (Exception exp) {

            }
        }
    }
}
