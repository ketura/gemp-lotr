package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardFromHandResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Wily Hillmen
 * Dunland	Condition • Support Area
 * Response: If a [Dunland] card is discarded from your hand, discard this condition to take that card back into hand
 * or stack it on a site you control.
 */
public class Card20_035 extends AbstractPermanent {
    public Card20_035() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "Wily Hillmen");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, final EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromHand(game, effectResult, Culture.DUNLAND)
                && PlayConditions.canSelfDiscard(self, game)) {
            final PhysicalCard discardedCard = ((DiscardCardFromHandResult) effectResult).getDiscardedCard();

            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));

            List<Effect> possibleEffects= new LinkedList<Effect>();
            if (PlayConditions.canSpot(game, Filters.siteControlled(playerId)))
                possibleEffects.add(
                        new ChooseActiveCardEffect(self, playerId, "Choose controlled site", Filters.siteControlled(playerId)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard site) {
                                action.appendEffect(
                                        new StackCardFromDiscardEffect(discardedCard, site));
                            }

                            @Override
                            public String getText(LotroGame game) {
                                return "Stack on a site you control";
                            }
                        });
            possibleEffects.add(
                    new PutCardFromDiscardIntoHandEffect(discardedCard));
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));

            return Collections.singletonList(action);
        }
        return null;
    }
}
