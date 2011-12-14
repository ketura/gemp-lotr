package com.gempukku.lotro.cards.set12.wraith;

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
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition to make a Nazgul strength +1 for each of the following that is true: he
 * is at a battleground or forest site; he is skirmishing a companion who has resistance 4 or less; he is bearing
 * a possession; he is in a fierce skirmish.
 */
public class Card12_182 extends AbstractPermanent {
    public Card12_182() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Unimpeded");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Nazgul", Race.NAZGUL) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int bonus = 0;
                            if (PlayConditions.location(game, Filters.or(Keyword.BATTLEGROUND, Keyword.FOREST)))
                                bonus++;
                            if (Filters.inSkirmishAgainst(CardType.COMPANION, Filters.maxResistance(4)).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                bonus++;
                            if (Filters.hasAttached(CardType.POSSESSION).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                bonus++;
                            if (game.getGameState().isFierceSkirmishes())
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
