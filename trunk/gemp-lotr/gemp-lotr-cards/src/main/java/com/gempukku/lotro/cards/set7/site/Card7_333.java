package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Type: Site
 * Site: 1K
 * Game Text: When the fellowship moves from this site, the Free Peoples player may spot 3 Hobbits to remove (2).
 */
public class Card7_333 extends AbstractSite {
    public Card7_333() {
        super("Sleeping Quarters", Block.KING, 1, 0, Direction.LEFT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.movesFrom(game, effectResult, self)
                && PlayConditions.canSpot(game, 3, Race.HOBBIT)
                && playerId.equals(game.getGameState().getCurrentPlayerId())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RemoveTwilightEffect(2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
