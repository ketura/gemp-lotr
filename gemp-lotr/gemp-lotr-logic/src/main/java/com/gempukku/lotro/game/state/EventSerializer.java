package com.gempukku.lotro.game.state;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.decisions.AwaitingDecision;
import com.gempukku.lotro.cards.lotronly.LotroDeck;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class EventSerializer {
    public Node serializeEvent(Document doc, GameEvent gameEvent) {
        Element eventElem = doc.createElement("ge");
        eventElem.setAttribute("type", gameEvent.getType().getCode());
        eventElem.setAttribute("timestamp", gameEvent.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSSS")));

        if (gameEvent.getBlueprintId() != null)
            eventElem.setAttribute("blueprintId", gameEvent.getBlueprintId());
        if (gameEvent.getCardId() != null)
            eventElem.setAttribute("cardId", gameEvent.getCardId().toString());
        if (gameEvent.getImageUrl() != null)
            eventElem.setAttribute("imageUrl", gameEvent.getImageUrl());
        if (gameEvent.getIndex() != null)
            eventElem.setAttribute("index", gameEvent.getIndex().toString());
        if (gameEvent.getControllerId() != null)
            eventElem.setAttribute("controllerId", gameEvent.getControllerId());
        if (gameEvent.getParticipantId() != null)
            eventElem.setAttribute("participantId", gameEvent.getParticipantId());
        if (gameEvent.getAllParticipantIds() != null)
            eventElem.setAttribute("allParticipantIds", listToCommaSeparated(gameEvent.getAllParticipantIds()));
        if (gameEvent.getPhase() != null)
            eventElem.setAttribute("phase", gameEvent.getPhase());
        if (gameEvent.getTargetCardId() != null)
            eventElem.setAttribute("targetCardId", gameEvent.getTargetCardId().toString());
        if (gameEvent.getZone() != null)
            eventElem.setAttribute("zone", gameEvent.getZone().name());
        if (gameEvent.getToken() != null)
            eventElem.setAttribute("token", gameEvent.getToken().name());
        if (gameEvent.getCount() != null)
            eventElem.setAttribute("count", gameEvent.getCount().toString());
        if (gameEvent.getOtherCardIds() != null)
            eventElem.setAttribute("otherCardIds", arrayToCommaSeparated(gameEvent.getOtherCardIds()));
        if (gameEvent.getMessage() != null)
            eventElem.setAttribute("message", gameEvent.getMessage());
        if (gameEvent.getVersion() != null)
            eventElem.setAttribute("version", gameEvent.getVersion().toString());
        if (gameEvent.getType() == GameEvent.Type.PARTICIPANTS)
            eventElem.setAttribute("discardPublic", String.valueOf(gameEvent.isDiscardPublic()));
        if (gameEvent.getGameStats() != null)
            serializeGameStats(doc, eventElem, gameEvent.getGameStats());
        if (gameEvent.getAwaitingDecision() != null)
            serializeDecision(doc, eventElem, gameEvent.getAwaitingDecision());
        if (gameEvent.getDecks() != null)
            serializeDecks(doc, eventElem, gameEvent.getDecks());

        return eventElem;
    }

    private static String arrayToCommaSeparated(int[] integers) {
        int iMax = integers.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(integers[i]);
            if (i == iMax)
                return b.toString();
            b.append(",");
        }
    }

    private static String listToCommaSeparated(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String participantId : strings) {
            if (!first) sb.append(",");
            sb.append(participantId);
            first = false;
        }
        return sb.toString();
    }

    private void serializeDecks(Document document, Element eventElem, Map<String, LotroDeck> decks) {
        for(var pair : decks.entrySet()) {
            String player = pair.getKey();
            var deck = pair.getValue();

            var deckElement = document.createElement("deckReadout");
            deckElement.setAttribute("playerId", player);
            deckElement.setAttribute("name", deck.getDeckName());
            deckElement.setAttribute("rb", deck.getRingBearer());
            deckElement.setAttribute("ring", deck.getRing());
            deckElement.setAttribute("sites", String.join(",", deck.getSites()));
            deckElement.setAttribute("deck", String.join(",", deck.getDrawDeckCards()));

            eventElem.appendChild(deckElement);
        }
    }

    private void serializeDecision(Document doc, Element eventElem, AwaitingDecision decision) {
        eventElem.setAttribute("id", String.valueOf(decision.getAwaitingDecisionId()));
        eventElem.setAttribute("decisionType", decision.getDecisionType().name());
        if (decision.getText() != null)
            eventElem.setAttribute("text", decision.getText());
        for (Map.Entry<String, String[]> paramEntry : decision.getDecisionParameters().entrySet()) {
            for (String value : paramEntry.getValue()) {
                Element decisionParam = doc.createElement("parameter");
                decisionParam.setAttribute("name", paramEntry.getKey());
                decisionParam.setAttribute("value", value);
                eventElem.appendChild(decisionParam);
            }
        }
    }

    private void serializeGameStats(Document doc, Element eventElem, GameStats gameStats) {
        for (Map.Entry<String, Map<Zone, Integer>> playerZoneSizes : gameStats.getZoneSizes().entrySet()) {
            final Element playerZonesElem = doc.createElement("playerZones");

            playerZonesElem.setAttribute("name", playerZoneSizes.getKey());

            for (Map.Entry<Zone, Integer> zoneSizes : playerZoneSizes.getValue().entrySet())
                playerZonesElem.setAttribute(zoneSizes.getKey().name(), zoneSizes.getValue().toString());

            eventElem.appendChild(playerZonesElem);
        }

        StringBuilder charStr = new StringBuilder();
        if (charStr.length() > 0)
            charStr.delete(0, 1);

        if (charStr.length() > 0)
            eventElem.setAttribute("charStats", charStr.toString());
    }

    public static void main(String[] args) {
        int[] array = new int[] {0, 1};
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int cardId : array) {
            if (!first) sb.append(",");
            sb.append(cardId);
            first = false;
        }
        System.out.println(sb.toString());
        System.out.println(arrayToCommaSeparated(array));
    }
}
