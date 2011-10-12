package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.StackCardFromHandEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area.
 * Skirmish: Exert a Dwarf and stack a Free Peoples card from hand here to prevent all wounds to that Dwarf.
 */
public class Card4_047 extends AbstractPermanent {
    public Card4_047() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "From the Armory");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF))
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.side(Side.FREE_PEOPLE)).size() > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.DWARF)) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendCost(
                                    new ChooseCardsFromHandEffect(playerId, 1, 1, Filters.side(Side.FREE_PEOPLE)) {
                                        @Override
                                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                            SubAction subAction = new SubAction(action);
                                            for (PhysicalCard selectedCard : selectedCards) {
                                                subAction.appendEffect(
                                                        new StackCardFromHandEffect(selectedCard, self));
                                            }
                                            game.getActionsEnvironment().addActionToStack(subAction);
                                        }
                                    });
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new CantTakeWoundsModifier(self, Filters.sameCard(character)), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
