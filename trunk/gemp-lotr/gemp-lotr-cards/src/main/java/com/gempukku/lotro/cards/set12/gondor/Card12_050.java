package com.gempukku.lotro.cards.set12.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Condition • Support Area
 * Game Text: Toil 2. (For each [GONDOR] character you exert when playing this, its twilight cost is -2) Skirmish: If
 * a [GONDOR] companion is not assigned to a skirmish, discard this condition to have him or her replace an unbound
 * companion skirmishing a minion.
 */
public class Card12_050 extends AbstractPermanent {
    public Card12_050() {
        super(Side.FREE_PEOPLE, 4, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Guardian");
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Culture.GONDOR, CardType.COMPANION, Filters.notAssignedToSkirmish)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose GONDONR companion", Culture.GONDOR, CardType.COMPANION, Filters.notAssignedToSkirmish) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new ReplaceInSkirmishEffect(card, Filters.unboundCompanion));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
