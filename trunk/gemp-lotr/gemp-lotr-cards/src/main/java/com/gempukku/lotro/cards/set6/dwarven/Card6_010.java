package com.gempukku.lotro.cards.set6.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. When you play this condition, you may spot a Dwarf to place 2 [DWARVEN] tokens
 * here. Fellowship: Discard this condition or remove a [DWARVEN] token from here to stack the top card of your
 * draw deck on a [DWARVEN] condition that has a card stacked on it.
 */
public class Card6_010 extends AbstractPermanent {
    public Card6_010() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Suspended Palaces", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Race.DWARF)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.DWARVEN, 2));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new DiscardCardsFromPlayEffect(self, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this condition";
                        }
                    });
            possibleCosts.add(
                    new RemoveTokenEffect(self, self, Token.DWARVEN) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove a DWARVEN token from here";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));

            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose condition", Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(Filters.any)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new StackTopCardsFromDeckEffect(self, playerId, 1, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
