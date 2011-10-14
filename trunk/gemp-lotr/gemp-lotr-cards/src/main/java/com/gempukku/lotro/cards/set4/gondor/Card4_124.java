package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

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
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Help in Doubt and Need");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(),
                Filters.or(
                        Filters.and(Filters.culture(Culture.GONDOR), Filters.unboundCompanion()),
                        Filters.and(Filters.culture(Culture.ROHAN), Filters.type(CardType.COMPANION))))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(
                            Filters.and(Filters.culture(Culture.GONDOR), Filters.unboundCompanion()),
                            Filters.and(Filters.culture(Culture.ROHAN), Filters.type(CardType.COMPANION)))) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            Culture culture = character.getBlueprint().getCulture();
                            Filter filter;
                            if (culture == Culture.GONDOR)
                                filter = Filters.and(Filters.culture(Culture.ROHAN), Filters.type(CardType.COMPANION));
                            else
                                filter = Filters.and(Filters.culture(Culture.GONDOR), Filters.unboundCompanion());

                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose companion", filter) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                                            action.insertEffect(
                                                    new AddUntilEndOfPhaseModifierEffect(
                                                            new StrengthModifier(self, Filters.sameCard(card), 2), Phase.SKIRMISH));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
