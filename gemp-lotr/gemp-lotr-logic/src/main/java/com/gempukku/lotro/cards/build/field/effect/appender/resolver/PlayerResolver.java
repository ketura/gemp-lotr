package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayerSource;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.GameUtils;

public class PlayerResolver {
    public static PlayerSource resolvePlayer(String type, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (type.equals("you"))
            return (actionContext) -> actionContext.getPerformingPlayer();
        if (type.equals("owner"))
            return (actionContext) -> actionContext.getSource().getOwner();
        else if (type.equals("shadowPlayer"))
            return (actionContext) -> GameUtils.getFirstShadowPlayer(actionContext.getGame());
        else if (type.equals("fp"))
            return ((actionContext) -> actionContext.getGame().getGameState().getCurrentPlayerId());
        else if (type.startsWith("ownerFromMemory(") && type.endsWith(")")) {
            String memory = type.substring(type.indexOf("(") + 1, type.lastIndexOf(")"));
            return (actionContext) -> {
                final PhysicalCard cardFromMemory = actionContext.getCardFromMemory(memory);
                if (cardFromMemory != null)
                    return cardFromMemory.getOwner();
                else
                    // Sensible default
                    return actionContext.getPerformingPlayer();
            };
        }
        throw new InvalidCardDefinitionException("Unable to resolve player resolver of type: " + type);
    }
}
