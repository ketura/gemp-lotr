package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Skirmish: Exert an unbound [GONDOR] Man to make a [ROHAN] companion
 * strength +2, or exert a [ROHAN] companion to make an unbound [GONDOR] Man strength +2.
 */
public class Card4_124 extends AbstractPermanent {
    public Card4_124() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, "Help in Doubt and Need");
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game,
                Filters.or(
                        Filters.and(Culture.GONDOR, Filters.unboundCompanion),
                        Filters.and(Culture.ROHAN, CardType.COMPANION)))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(
                            Filters.and(Culture.GONDOR, Filters.unboundCompanion),
                            Filters.and(Culture.ROHAN, CardType.COMPANION))) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            Culture culture = character.getBlueprint().getCulture();
                            Filter filter;
                            if (culture == Culture.GONDOR)
                                filter = Filters.and(Culture.ROHAN, CardType.COMPANION);
                            else
                                filter = Filters.and(Culture.GONDOR, Filters.unboundCompanion);

                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose companion", filter) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                                            action.insertEffect(
                                                    new AddUntilEndOfPhaseModifierEffect(
                                                            new StrengthModifier(self, Filters.sameCard(card), 2)));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
