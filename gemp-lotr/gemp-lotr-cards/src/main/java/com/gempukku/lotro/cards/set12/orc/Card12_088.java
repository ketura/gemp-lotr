package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PutCardFromHandOnTopOfDeckEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: At the start of each skirmish involving an [ORC] minion, you may spot 6 companions to exert a companion in
 * that skirmish. Shadow: Discard this condition and spot an [ORC] minion to make the Free Peoples player place a card
 * from his or her hand on top of his or her draw deck.
 */
public class Card12_088 extends AbstractPermanent {
    public Card12_088() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "Great Cost");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION, Filters.inSkirmish)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseCardsFromHandEffect(game.getGameState().getCurrentPlayerId(), 1, 1, Filters.any) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards) {
                                action.appendEffect(
                                        new PutCardFromHandOnTopOfDeckEffect(selectedCard));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
