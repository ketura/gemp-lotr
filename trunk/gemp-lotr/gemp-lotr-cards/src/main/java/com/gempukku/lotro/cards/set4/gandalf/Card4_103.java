package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Ally • Home 2T & 8T • Ent
 * Strength: 12
 * Vitality: 4
 * Site: 2T, 8T
 * Game Text: Unhasty. Response: If an unbound Hobbit is about to be discarded, stack him here instead.
 * Fellowship: Exert Treebeard and add(1) to play an unbound Hobbit stacked here.
 */
public class Card4_103 extends AbstractAlly {
    public Card4_103() {
        super(4, Block.TWO_TOWERS, new int[]{2, 8}, 12, 4, Race.ENT, Culture.GANDALF, "Treebeard", true);
        addKeyword(Keyword.UNHASTY);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, final PhysicalCard self) {
        if (PlayConditions.isGettingDiscarded(effect, game, Race.HOBBIT, Filters.unboundCompanion)) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            Collection<PhysicalCard> discardedHobbits = Filters.filter(discardEffect.getAffectedCardsMinusPrevented(game), game.getGameState(), game.getModifiersQuerying(), Filters.unboundCompanion, Race.HOBBIT);
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound hobbit", Filters.in(discardedHobbits)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new StackCardFromPlayEffect(card, self));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, self)
                && Filters.filter(game.getGameState().getStackedCards(self), game.getGameState(), game.getModifiersQuerying(), Filters.unboundCompanion, Race.HOBBIT, Filters.playable(game)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new AddTwilightEffect(self, 1));
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose an Orc to play", game.getGameState().getStackedCards(self), Filters.playable(game), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> stackedHobbits) {
                            if (stackedHobbits.size() > 0) {
                                PhysicalCard stackedHobbit = stackedHobbits.iterator().next();
                                game.getActionsEnvironment().addActionToStack(stackedHobbit.getBlueprint().getPlayCardAction(playerId, game, stackedHobbit, 0, false));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
