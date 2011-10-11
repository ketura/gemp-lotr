package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 8
 * Type: Site
 * Site: 9
 * Game Text: When the fellowship moves to Summit of Amon Hen, each Shadow player may draw a card for each burden.
 */
public class Card1_362 extends AbstractSite {
    public Card1_362() {
        super("Summit of Amon Hen", Block.FELLOWSHIP, 9, 8, Direction.LEFT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && game.getGameState().getCurrentSite() == self) {
            if (!playerId.equals(game.getGameState().getCurrentPlayerId())) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new DrawCardEffect(playerId, game.getGameState().getBurdens()));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
