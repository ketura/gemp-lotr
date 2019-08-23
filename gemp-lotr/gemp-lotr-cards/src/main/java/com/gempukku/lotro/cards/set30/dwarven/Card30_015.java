package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;
 
 /**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Companion • Dwarf
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Maneuver: Exert Nori twice and discard a [DWARVEN] follower to discard a possession
 */
public class Card30_015 extends AbstractCompanion {
    public Card30_015() {
        super(3, 7, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Nori", "Of the Blue Mountains", true);
    }


    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, 2, self)
				&& PlayConditions.canDiscardFromPlay(self, game, Culture.DWARVEN, CardType.FOLLOWER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
			action.appendCost(
                    new SelfExertEffect(action, self));
			action.appendCost(
					new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.FOLLOWER));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
