package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.PreventAllWoundsActionProxy;
import com.gempukku.lotro.logic.effects.StackCardFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.DWARVEN, "From the Armory");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Race.DWARF)
                && Filters.filter(game.getGameState().getHand(playerId), game, Side.FREE_PEOPLE).size() > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendCost(
                                    new ChooseCardsFromHandEffect(playerId, 1, 1, Side.FREE_PEOPLE) {
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
                                    new AddUntilEndOfPhaseActionProxyEffect(
                                            new PreventAllWoundsActionProxy(self, character)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
