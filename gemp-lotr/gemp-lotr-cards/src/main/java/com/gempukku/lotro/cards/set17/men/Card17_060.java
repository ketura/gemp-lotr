package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Maneuver: Spot a [MEN] possession and discard this minion from play to make each [MEN] minion you spot
 * strength +2 until the start of the regroup phase.
 */
public class Card17_060 extends AbstractMinion {
    public Card17_060() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Sunland Warrior");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSpot(game, Culture.MEN, CardType.POSSESSION)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardsEffect(self, playerId, "Choose minions to give strength bonus to", 1, Integer.MAX_VALUE, Culture.MEN, CardType.MINION) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.in(cards), 2), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
