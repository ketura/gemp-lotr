package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.PlayerResolver;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import org.json.simple.JSONObject;

public class CantLookOrRevealHand implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "player", "hand");

        final String player = FieldUtils.getString(object.get("player"), "player");
        final String hand = FieldUtils.getString(object.get("hand"), "hand");

        PlayerSource playerSource = PlayerResolver.resolvePlayer(player, environment);
        PlayerSource handSource = PlayerResolver.resolvePlayer(hand, environment);

        return actionContext -> new AbstractModifier(actionContext.getSource(), "Player may not look at or reveal cards in another player hand",
                null, ModifierEffect.LOOK_OR_REVEAL_MODIFIER) {
            @Override
            public boolean canLookOrRevealCardsInHand(DefaultGame game, String revealingPlayerId, String actingPlayerId) {
                if (playerSource.getPlayer(actionContext).equals(actingPlayerId)
                        && handSource.getPlayer(actionContext).equals(revealingPlayerId))
                    return false;
                return true;
            }
        };
    }
}
