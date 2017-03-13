package com.gempukku.lotro.cards.set21.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

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
 * Game Text: Skirmish: Exert Nori twice and discard a [DWARVEN] follower to discard a possession
 */
public class Card21_15 extends AbstractCompanion {
    public Card21_15() {
        super(3, 7, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Nori", "Of the Blue Mountains", true);
    }


    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 2, self))
				&& PlayConditions.canDiscardFromPlay(self, game, Culture.DWARVEN, CardType.FOLLOWER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
			action.appendCost(
                    new SelfExertEffect(action, self));
			action.appendCost(
					new ChooseAndDiscardCardsFromPlayEffect(action, playerID, 1, 1, Culture.DWARF, CardType.FOLLOWER));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}