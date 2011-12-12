package com.gempukku.lotro.cards.set12.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition to make a [GONDOR] Man strength +1 for each of the following that
 * is true: he or she is at a battleground site; he or she has resistance 4 or more; he or she is bearing a hand weapon;
 * he or she is skirmishing a roaming minion.
 */
public class Card12_052 extends AbstractPermanent {
    public Card12_052() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Tireless");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a GONDOR Man", Culture.GONDOR, Race.MAN) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int bonus = 0;
                            if (PlayConditions.location(game, Keyword.BATTLEGROUND))
                                bonus++;
                            if (Filters.minResistance(4).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                bonus++;
                            if (Filters.hasAttached(PossessionClass.HAND_WEAPON).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                bonus++;
                            if (Filters.inSkirmishAgainst(Filters.roamingMinion).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                bonus++;
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, bonus), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
