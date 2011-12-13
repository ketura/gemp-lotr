package com.gempukku.lotro.cards.set12.rohan;

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
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition to make a [ROHAN] companion strength +1 for each of the following that
 * is true: he or she is at a plains site; he or she has resistance 4 or more; he or she is bearing armor; he or she
 * is skirmishing a wounded minion.
 */
public class Card12_110 extends AbstractPermanent {
    public Card12_110() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Cleaving a Path");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a ROHAN companion", Culture.ROHAN, CardType.COMPANION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int bonus = 0;
                            if (PlayConditions.location(game, Keyword.PLAINS))
                                bonus++;
                            if (Filters.minResistance(4).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                bonus++;
                            if (Filters.hasAttached(PossessionClass.ARMOR).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                bonus++;
                            if (Filters.inSkirmishAgainst(CardType.MINION, Filters.wounded).accepts(game.getGameState(), game.getModifiersQuerying(), card))
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
