package com.gempukku.lotro.cards.set6.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
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
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
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
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. When you play this condition, you may spot 2 [DUNLAND] Men to place
 * 2 [DUNLAND] tokens here. Response: If a [DUNLAND] Man is about to take a wound, discard this condition or remove
 * a [DUNLAND] token from here to prevent that wound.
 */
public class Card6_001 extends AbstractPermanent {
    public Card6_001() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "Bound By Rage", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, 2, Culture.DUNLAND, Race.MAN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.DUNLAND, 2));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.DUNLAND, Race.MAN)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final Collection<PhysicalCard> cardsToBeWounded = woundEffect.getAffectedCardsMinusPrevented(game);
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
                    new RemoveTokenEffect(self, self, Token.DUNLAND) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove a DUNLAND token from here";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose DUNLAND Man", Culture.DUNLAND, Race.MAN, Filters.in(cardsToBeWounded)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard dunlandMan) {
                            action.appendEffect(
                                    new PreventCardEffect(woundEffect, dunlandMan));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
