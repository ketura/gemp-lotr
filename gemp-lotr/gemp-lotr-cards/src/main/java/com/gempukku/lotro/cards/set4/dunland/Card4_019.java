package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Plays to your support area. When you play this possession, you may draw a card. Response: If a [DUNLAND]
 * Man is about to take a wound, remove (2) or discard this possession to prevent that wound.
 */
public class Card4_019 extends AbstractPermanent {
    public Card4_019() {
        super(Side.SHADOW, 1, CardType.POSSESSION, Culture.DUNLAND, Zone.SUPPORT, "Hides");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.sameCard(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.DUNLAND, Race.MAN)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            Collection<PhysicalCard> woundedCharacters = woundEffect.getAffectedCardsMinusPrevented(game);
            final ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new RemoveTwilightEffect(2) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove (2)";
                        }
                    });
            possibleCosts.add(
                    new DiscardCardsFromPlayEffect(self, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this possession";
                        }
                    });

            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose DUNLAND Man", Filters.in(woundedCharacters), Culture.DUNLAND, Race.MAN) {
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
