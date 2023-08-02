package com.gempukku.lotro.rules;

import com.gempukku.lotro.adventure.InvalidSoloAdventureException;
import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.game.PlayerOrder;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.rules.lotronly.LotroGameUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameUtils {
    public static boolean isCurrentPlayer(DefaultGame game, String playerId) {
        return game.getGameState().getCurrentPlayerId().equals(playerId);
    }

    public static String getFullName(LotroPhysicalCard card) {
        LotroCardBlueprint blueprint = card.getBlueprint();
        return getFullName(blueprint);
    }

    public static String getFullName(LotroCardBlueprint blueprint) {
        if (blueprint.getSubtitle() != null)
            return blueprint.getTitle() + ", " + blueprint.getSubtitle();
        return blueprint.getTitle();
    }

    public static String[] getOpponents(DefaultGame game, String playerId) {
        if (game.isSolo())
            throw new InvalidSoloAdventureException("Opponent requested");
        List<String> shadowPlayers = new LinkedList<>(game.getGameState().getPlayerOrder().getAllPlayers());
        shadowPlayers.remove(playerId);
        return shadowPlayers.toArray(new String[shadowPlayers.size()]);
    }

    public static String[] getAllPlayers(DefaultGame game) {
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

    public static List<LotroPhysicalCard> getRandomCards(List<? extends LotroPhysicalCard> cards, int count) {
        List<LotroPhysicalCard> randomizedCards = new ArrayList<>(cards);
        Collections.shuffle(randomizedCards, ThreadLocalRandom.current());

        return new LinkedList<>(randomizedCards.subList(0, Math.min(count, randomizedCards.size())));
    }

    public static String s(Collection<LotroPhysicalCard> cards) {
        if (cards.size() > 1)
            return "s";
        return "";
    }

    public static String be(Collection<LotroPhysicalCard> cards) {
        if (cards.size() > 1)
            return "are";
        return "is";
    }

    public static String getCardLink(LotroPhysicalCard card) {
        LotroCardBlueprint blueprint = card.getBlueprint();
        return getCardLink(card.getBlueprintId(), blueprint);
    }

    public static String getCardLink(String blueprintId, LotroCardBlueprint blueprint) {
        return "<div class='cardHint' value='" + blueprintId + "'>" + (blueprint.isUnique() ? "·" : "") + GameUtils.getFullName(blueprint) + "</div>";
    }

    public static String getAppendedTextNames(Collection<? extends LotroPhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (LotroPhysicalCard card : cards)
            sb.append(GameUtils.getFullName(card) + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    public static String getAppendedNames(Collection<? extends LotroPhysicalCard> cards) {
        ArrayList<String> cardStrings = new ArrayList<>();
        for (LotroPhysicalCard card : cards) {
            cardStrings.add(GameUtils.getCardLink(card));
        }

        if (cardStrings.size() == 0)
            return "none";

        return String.join(", ", cardStrings);
    }

    public static String formatNumber(int effective, int requested) {
        if (effective != requested)
            return effective + "(out of " + requested + ")";
        else
            return String.valueOf(effective);
    }
    public static String SubstituteText(String text)
    {
        return SubstituteText(text, null);
    }

    public static String SubstituteText(String text, DefaultActionContext context)
    {
        String result = text;
        while (result.contains("{")) {
            int startIndex = result.indexOf("{");
            int endIndex = result.indexOf("}");
            String memory = result.substring(startIndex + 1, endIndex);
//            String culture = getCultureImage(memory);
//            if(culture != null) {
//                result = result.replace("{" + memory + "}", culture);
//            }
            if(context != null){
                String cardNames = LotroGameUtils.getAppendedNames(context.getCardsFromMemory(memory));
                if(cardNames.equalsIgnoreCase("none")) {
                    try {
                        cardNames = context.getValueFromMemory(memory);
                    }
                    catch(IllegalArgumentException ex) {
                        cardNames = "none";
                    }
                }
                result = result.replace("{" + memory + "}", cardNames);
            }
        }

        return result;
    }

    // As of 7/12/23, this function likely doesn't work (except in the inherited LotrGameUtils class)
    public static String getDeluxeCardLink(String blueprintId, LotroCardBlueprint blueprint) {
        var cultureString = "";
        return "<div class='cardHint' value='" + blueprintId + "'>" + cultureString
                + (blueprint.isUnique() ? "·" : "") + " " + LotroGameUtils.getFullName(blueprint) + "</div>";
    }

}