package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 3
 * Site: 6
 * Game Text: Response: If this minion wins a skirmish, remove (2) to make the Free Peoples player discard a card
 * at random from hand.
 */
public class Card1_240 extends AbstractMinion {
    public Card1_240() {
        super(4, 12, 3, 6, Race.ORC, Culture.SAURON, "Band of the Eye");
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self)
                && PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), null, self, 2)) {
            ActivateCardAction action = new ActivateCardAction(self, null);
            action.appendCost(new RemoveTwilightEffect(2));
            action.appendEffect(
                    new DiscardCardAtRandomFromHandEffect(self, game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
