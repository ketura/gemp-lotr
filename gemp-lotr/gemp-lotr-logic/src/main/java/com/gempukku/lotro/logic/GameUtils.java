package com.gempukku.lotro.logic;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.*;

public class GameUtils {
    public static String[] getOpponents(LotroGame game, String playerId) {
        List<String> shadowPlayers = new LinkedList<String>(game.getGameState().getPlayerOrder().getAllPlayers());
        shadowPlayers.remove(playerId);
        return shadowPlayers.toArray(new String[shadowPlayers.size()]);
    }

    public static String[] getAllPlayers(LotroGame game) {
        return game.getGameState().getPlayerOrder().getAllPlayers().toArray(new String[0]);
    }

    public static List<PhysicalCard> getRandomCards(List<? extends PhysicalCard> cards, int count) {
        List<PhysicalCard> randomizedCards = new ArrayList<PhysicalCard>(cards);
        Collections.shuffle(randomizedCards);

        return new LinkedList<PhysicalCard>(randomizedCards.subList(0, Math.min(count, randomizedCards.size())));
    }

    public static String s(Collection<PhysicalCard> cards) {
        if (cards.size() > 1)
            return "s";
        return "";
    }

    public static String be(Collection<PhysicalCard> cards) {
        if (cards.size() > 1)
            return "are";
        return "is";
    }

    public static String getCardLink(PhysicalCard card) {
        return "<div class='cardHint' value='" + card.getBlueprintId() + "'>" + card.getBlueprint().getName() + "</div>";
    }
}
