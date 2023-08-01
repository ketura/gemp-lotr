package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.adventure.InvalidSoloAdventureException;
import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.rules.GameUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LotroGameUtils extends GameUtils {
    public static Side getSide(DefaultGame game, String playerId) {
        return isFP(game, playerId) ? Side.FREE_PEOPLE : Side.SHADOW;
    }

    public static boolean isSide(DefaultGame game, Side side, String playerId) {
        if (side == Side.FREE_PEOPLE)
            return game.getGameState().getCurrentPlayerId().equals(playerId);
        else
            return !game.getGameState().getCurrentPlayerId().equals(playerId);
    }

    public static boolean isFP(DefaultGame game, String playerId) {
        return game.getGameState().getCurrentPlayerId().equals(playerId);
    }

    public static boolean isShadow(DefaultGame game, String playerId) {
        return !game.getGameState().getCurrentPlayerId().equals(playerId);
    }

    public static String getFirstShadowPlayer(DefaultGame game) {
        if (game.isSolo())
            throw new InvalidSoloAdventureException("Shadow player requested");
        final String fpPlayer = game.getGameState().getCurrentPlayerId();
        final PlayOrder counterClockwisePlayOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(fpPlayer, false);
        // Skip FP player
        counterClockwisePlayOrder.getNextPlayer();
        return counterClockwisePlayOrder.getNextPlayer();
    }

    public static String[] getShadowPlayers(DefaultGame game) {
        if (game.isSolo())
            throw new InvalidSoloAdventureException("Shadow player requested");
        final String fpPlayer = game.getGameState().getCurrentPlayerId();
        List<String> shadowPlayers = new LinkedList<>(game.getGameState().getPlayerOrder().getAllPlayers());
        shadowPlayers.remove(fpPlayer);
        return shadowPlayers.toArray(new String[shadowPlayers.size()]);
    }

    public static String getFreePeoplePlayer(DefaultGame game) {
        return game.getGameState().getCurrentPlayerId();
    }

    public static String getDeluxeCardLink(String blueprintId, LotroCardBlueprint blueprint) {
        var culture = blueprint.getCulture();
        var cultureString = "";
        if(culture == null) {
            if (blueprint.getTitle().equals("The One Ring")) {
                cultureString = getCultureImage(culture, "one_ring");
            }
            else {
                cultureString = getCultureImage(culture, "site");
            }
        }
        else {
            cultureString = getCultureImage(culture, null);
        }
        return "<div class='cardHint' value='" + blueprintId + "'>" + cultureString
                + (blueprint.isUnique() ? "·" : "") + " " + LotroGameUtils.getFullName(blueprint) + "</div>";
    }

    public static String getCultureImage(String cultureName) {
        Culture culture = Culture.findCulture(cultureName);
        if(culture == null)
            return null;

        return getCultureImage(culture);
    }

    public static String getCultureImage(Culture culture, String override) {
        if(override == null || override.isEmpty()) {
            override = culture.toString().toLowerCase();
        }
        return "<span class='cultureHint' ><img src='images/cultures/" + override + ".png'></span>";
    }

    public static String getCultureImage(Culture culture) {
        return "<span class='cultureHint' value='" + culture.toString() + "'><img src='images/cultures/" + culture.toString().toLowerCase() + ".png'> "
                + culture.getHumanReadable() + "</span>";
    }

    public static String SubstituteText(String text, DefaultActionContext context)
    {
        String result = text;
        while (result.contains("{")) {
            int startIndex = result.indexOf("{");
            int endIndex = result.indexOf("}");
            String memory = result.substring(startIndex + 1, endIndex);
            String culture = getCultureImage(memory);
            if(culture != null) {
                result = result.replace("{" + memory + "}", culture);
            }
            else if(context != null){
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

    // "If you can spot X [elven] tokens..."
    public static int getSpottableTokensTotal(DefaultGame game, Token token) {
        return getSpottableCultureTokensOfType(game, token, Filters.any);
    }

    // "If you can spot X [elven] tokens on conditions..."
    public static int getSpottableCultureTokensOfType(DefaultGame game, Token token, Filterable... filters) {
        int tokensTotal = 0;

        final var cards = Filters.filterActive(game, Filters.and(filters, Filters.hasToken(token)));

        for (LotroPhysicalCard physicalCard : cards)
            tokensTotal += game.getGameState().getTokenCount(physicalCard, token);

        return tokensTotal;
    }

    // "If you can spot X culture tokens on conditions..."
    public static int getAllSpottableCultureTokens(DefaultGame game, Filterable... filters) {
        int tokensTotal = 0;

        final var cards = Filters.filterActive(game, Filters.and(filters, Filters.hasAnyCultureTokens()));

        for (LotroPhysicalCard physicalCard : cards) {
            var tokens = game.getGameState().getTokens(physicalCard);
            for(var token : tokens.entrySet()) {
                if(token.getKey().getCulture() != null) {
                    tokensTotal += token.getValue();
                }
            }
        }

        return tokensTotal;
    }

    public static int getSpottableCulturesCount(DefaultGame game, Filterable... filters) {
        Set<Culture> cultures = new HashSet<>();
        for (LotroPhysicalCard physicalCard : Filters.filterActive(game, filters)) {
            final Culture culture = physicalCard.getBlueprint().getCulture();
            if (culture != null)
                cultures.add(culture);
        }
        return cultures.size();
    }

    public static int getSpottableRacesCount(DefaultGame game, Filterable... filters) {
        Set<Race> races = new HashSet<>();
        for (LotroPhysicalCard physicalCard : Filters.filterActive(game, filters)) {
            final Race race = physicalCard.getBlueprint().getRace();
            if (race != null)
                races.add(race);
        }
        return races.size();
    }

    public static int getSpottableFPCulturesCount(DefaultGame game, String playerId) {
        return game.getModifiersQuerying().getNumberOfSpottableFPCultures(game, playerId);
    }

    public static int getSpottableShadowCulturesCount(DefaultGame game, String playerId) {
        return game.getModifiersQuerying().getNumberOfSpottableShadowCultures(game, playerId);
    }

    public static int getRegion(DefaultGame game) {
        return getRegion(game.getGameState().getCurrentSiteNumber());
    }

    public static int getRegion(int siteNumber) {
        return 1 + ((siteNumber - 1) / 3);
    }

}
