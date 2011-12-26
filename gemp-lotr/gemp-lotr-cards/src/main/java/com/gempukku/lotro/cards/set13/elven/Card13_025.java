package com.gempukku.lotro.cards.set13.elven;

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
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition from play to make a minion skirmishing an Elf strength -1 for each of
 * the following that is true: it is skirmishing a companion who has resistance 4 or more; it is wounded; you can spot
 * an Elf archer; the fellowship is at a forest site.
 */
public class Card13_025 extends AbstractPermanent {
    public Card13_025() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Standing Tall");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Race.ELF)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int penalty = 0;
                            if (Filters.inSkirmishAgainst(CardType.COMPANION, Filters.minResistance(4)).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                penalty--;
                            if (Filters.wounded.accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                penalty--;
                            if (PlayConditions.canSpot(game, Race.ELF, Keyword.ARCHER))
                                penalty--;
                            if (PlayConditions.location(game, Keyword.FOREST))
                                penalty--;
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, penalty), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
