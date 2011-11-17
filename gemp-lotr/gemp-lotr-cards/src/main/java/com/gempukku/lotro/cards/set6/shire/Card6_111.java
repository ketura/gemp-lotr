package com.gempukku.lotro.cards.set6.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. When you play this condition, place 2 [SHIRE] tokens here. Response: If
 * an unbound Hobbit is about to take a wound, discard this condition or remove a [SHIRE] token from here to prevent
 * that wound.
 */
public class Card6_111 extends AbstractPermanent {
    public Card6_111() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Kept Safe", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SHIRE, 2));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.HOBBIT, Filters.unboundCompanion)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCharacters = woundEffect.getAffectedCardsMinusPrevented(game);

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
                    new RemoveTokenEffect(self, self, Token.SHIRE) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove a SHIRE token from here";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound Hobbit", Race.HOBBIT, Filters.unboundCompanion, Filters.in(woundedCharacters)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new PreventCardEffect(woundEffect, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
