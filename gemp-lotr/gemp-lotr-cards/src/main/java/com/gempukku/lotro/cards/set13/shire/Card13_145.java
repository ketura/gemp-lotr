package com.gempukku.lotro.cards.set13.shire;

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
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition to make a Hobbit strength +1 for each of the following that is true: he
 * or she is participating in a fierce skirmish; he or she has resistance 7 or more; he or she is at a dwelling site;
 * you can spot an unwounded Hobbit.
 */
public class Card13_145 extends AbstractPermanent {
    public Card13_145() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Don't Let Go");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard hobbit) {
                            int bonus = 0;
                            if (PlayConditions.canSpot(game, hobbit, Filters.inSkirmish) && game.getGameState().isFierceSkirmishes())
                                bonus++;
                            if (PlayConditions.canSpot(game, hobbit, Filters.minResistance(7)))
                                bonus++;
                            if (PlayConditions.location(game, Keyword.DWELLING))
                                bonus++;
                            if (PlayConditions.canSpot(game, Race.HOBBIT, Filters.unwounded))
                                bonus++;
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, hobbit, bonus), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
