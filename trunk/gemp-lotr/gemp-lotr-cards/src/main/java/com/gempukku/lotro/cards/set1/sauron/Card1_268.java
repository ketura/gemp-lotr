package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardCardAtRandomFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: When you play this minion, you may make the Free Peoples player discard a card at random from hand.
 */
public class Card1_268 extends AbstractMinion {
    public Card1_268() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Orc Inquisitor");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self, null, "Make the Free Peoples player discard a card at random from hand.");
            action.addEffect(
                    new DiscardCardAtRandomFromHandEffect(game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
