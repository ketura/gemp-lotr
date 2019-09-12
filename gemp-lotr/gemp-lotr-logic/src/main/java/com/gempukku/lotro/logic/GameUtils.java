package com.gempukku.lotro.logic;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.*;

public class GameUtils {
    public static Side getSide(LotroGame game, String playerId) {
        return isFP(game, playerId) ? Side.FREE_PEOPLE : Side.SHADOW;
    }

    public static boolean isSide(LotroGame game, Side side, String playerId) {
        if (side == Side.FREE_PEOPLE)
            return game.getGameState().getCurrentPlayerId().equals(playerId);
        else
            return !game.getGameState().getCurrentPlayerId().equals(playerId);
    }

    public static boolean isFP(LotroGame game, String playerId) {
        return game.getGameState().getCurrentPlayerId().equals(playerId);
    }

    public static boolean isShadow(LotroGame game, String playerId) {
        return !game.getGameState().getCurrentPlayerId().equals(playerId);
    }

    public static String getFullName(PhysicalCard card) {
        LotroCardBlueprint blueprint = card.getBlueprint();
        return getFullName(blueprint);
    }

    public static String getFullName(LotroCardBlueprint blueprint) {
        if (blueprint.getSubtitle() != null)
            return blueprint.getTitle() + ", " + blueprint.getSubtitle();
        return blueprint.getTitle();
    }

    public static String getFirstShadowPlayer(LotroGame game) {
        if (game.isSolo())
            throw new InvalidSoloAdventureException("Shadow player requested");
        final String fpPlayer = game.getGameState().getCurrentPlayerId();
        final PlayOrder counterClockwisePlayOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(fpPlayer, false);
        // Skip FP player
        counterClockwisePlayOrder.getNextPlayer();
        return counterClockwisePlayOrder.getNextPlayer();
    }

    public static String[] getShadowPlayers(LotroGame game) {
        if (game.isSolo())
            throw new InvalidSoloAdventureException("Shadow player requested");
        final String fpPlayer = game.getGameState().getCurrentPlayerId();
        List<String> shadowPlayers = new LinkedList<String>(game.getGameState().getPlayerOrder().getAllPlayers());
        shadowPlayers.remove(fpPlayer);
        return shadowPlayers.toArray(new String[shadowPlayers.size()]);
    }

    public static String getFreePeoplePlayer(LotroGame game) {
        return game.getGameState().getCurrentPlayerId();
    }

    public static String[] getOpponents(LotroGame game, String playerId) {
        if (game.isSolo())
            throw new InvalidSoloAdventureException("Opponent requested");
        List<String> shadowPlayers = new LinkedList<String>(game.getGameState().getPlayerOrder().getAllPlayers());
        shadowPlayers.remove(playerId);
        return shadowPlayers.toArray(new String[shadowPlayers.size()]);
    }

    public static String[] getAllPlayers(LotroGame game) {
        final GameState gameState = game.getGameState();
        final PlayerOrder playerOrder = gameState.getPlayerOrder();
        String[] result = new String[playerOrder.getPlayerCount()];

        final PlayOrder counterClockwisePlayOrder = playerOrder.getCounterClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
        int index = 0;

        String nextPlayer;
        while ((nextPlayer = counterClockwisePlayOrder.getNextPlayer()) != null) {
            result[index++] = nextPlayer;
        }
        return result;
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
        LotroCardBlueprint blueprint = card.getBlueprint();
        return getCardLink(card.getBlueprintId(), blueprint);
    }

    public static String getCardLink(String blueprintId, LotroCardBlueprint blueprint) {
        return "<div class='cardHint' value='" + blueprintId + "'>" + (blueprint.isUnique() ? "•" : "") + GameUtils.getFullName(blueprint) + "</div>";
    }

    public static String getAppendedTextNames(Collection<? extends PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(GameUtils.getFullName(card) + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    public static String getAppendedNames(Collection<? extends PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(GameUtils.getCardLink(card) + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    public static int getSpottableTokensTotal(LotroGame game, Token token) {
        int tokensTotal = 0;

        for (PhysicalCard physicalCard : Filters.filterActive(game, Filters.hasToken(token)))
            tokensTotal += game.getGameState().getTokenCount(physicalCard, token);

        return tokensTotal;
    }

    public static int getSpottableCulturesCount(LotroGame game, Filterable... filters) {
        Set<Culture> cultures = new HashSet<Culture>();
        for (PhysicalCard physicalCard : Filters.filterActive(game, filters)) {
            final Culture culture = physicalCard.getBlueprint().getCulture();
            if (culture != null)
                cultures.add(culture);
        }
        return cultures.size();
    }

    public static String formatNumber(int effective, int requested) {
        if (effective != requested)
            return effective + "(out of " + requested + ")";
        else
            return String.valueOf(effective);
    }

    public static int getRegion(LotroGame game) {
        return getRegion(game.getGameState().getCurrentSiteNumber());
    }

    public static int getRegion(int siteNumber) {
        return 1 + ((siteNumber - 1) / 3);
    }

    public static int getSpottableFPCulturesCount(LotroGame game, String playerId) {
        return game.getModifiersQuerying().getNumberOfSpottableFPCultures(game, playerId);
    }

    public static int getSpottableShadowCulturesCount(LotroGame game, String playerId) {
        return game.getModifiersQuerying().getNumberOfSpottableShadowCultures(game, playerId);
    }
}
