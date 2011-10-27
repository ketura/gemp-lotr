package com.gempukku.lotro.cards.set7.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: When you play this condition, you may stack 2 cards from hand here and draw a card for each [DWARVEN] card
 * you stack. Skirmish: Make a Dwarf strength +2. Also, make that Dwarf damage +2 for each [DWARVEN] card stacked
 * on this condition. Discard this condition.
 */
public class Card7_010 extends AbstractPermanent {
    public Card7_010() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Loyalty Unshaken");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndStackCardsFromHandEffect(action, playerId, 2, 2, self, Filters.any) {
                        @Override
                        public void stackFromHandCallback(Collection<PhysicalCard> cardsStacked) {
                            int count = Filters.filter(cardsStacked, game.getGameState(), game.getModifiersQuerying(), Culture.DWARVEN).size();
                            if (count > 0)
                                action.appendEffect(
                                        new DrawCardEffect(playerId, count));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                    new StrengthModifier(self, card, 2), Phase.SKIRMISH);
                            int stackedDwarf = Filters.filter(game.getGameState().getStackedCards(self), game.getGameState(), game.getModifiersQuerying(), Culture.DWARVEN).size();
                            if (stackedDwarf > 0)
                                game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                        new KeywordModifier(self, card, Keyword.DAMAGE, stackedDwarf * 2), Phase.SKIRMISH);
                        }
                    });
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
