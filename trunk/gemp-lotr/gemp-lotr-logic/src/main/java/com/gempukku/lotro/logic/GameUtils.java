package com.gempukku.lotro.logic;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

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

    public static String getAppendedTextNames(Collection<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(card.getBlueprint().getName() + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    public static final String getAppendedNames(Collection<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(GameUtils.getCardLink(card) + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    public static int getSpottableTokensTotal(GameState gameState, ModifiersQuerying modifiersQuerying, Token token) {
        int tokensTotal = 0;

        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Filters.hasToken(token)))
            tokensTotal += gameState.getTokenCount(physicalCard, token);

        return tokensTotal;
    }
}
