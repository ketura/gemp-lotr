package com.gempukku.lotro.cards.set17.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.ShuffleDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDeckEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Pipeweed. When you play this, you may search your deck for a [GANDALF] card and place it in your discard
 * pile. Regroup: Discard another pipeweed from play to discard a condition. Discard this condition.
 */
public class Card17_021 extends AbstractPermanent {
    public Card17_021() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GANDALF, "Long-stemmed Pipe");
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseCardsFromDeckEffect(playerId, 1, 1, Culture.GANDALF) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                action.appendEffect(
                                        new DiscardCardFromDeckEffect(card));
                            }
                        }
                    });
            action.appendEffect(
                    new ShuffleDeckEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.not(self), Keyword.PIPEWEED)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.not(self), Keyword.PIPEWEED));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
