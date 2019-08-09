package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardFromHandResult;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * •Westfold in Flames
 * Condition • Support Area
 * Each time you discard a [Dunland] card from hand during the Shadow phase, you may stack that card here.
 * Shadow or Skirmish: Spot 3 [Dunland] cards stacked here to play a card stacked here as if from hand.
 * http://lotrtcg.org/coreset/dunland/westfoldinflames(r1).png
 */
public class Card20_034 extends AbstractPermanent {
    public Card20_034() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.DUNLAND, "Westfold in Flames", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromHand(game, effectResult, Culture.DUNLAND)
                && PlayConditions.isPhase(game, Phase.SHADOW)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndStackCardsFromDiscardEffect(action, playerId, 1, 1, self, ((DiscardCardFromHandResult) effectResult).getDiscardedCard()));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if ((PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                || PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0))
                && Filters.filter(game.getGameState().getStackedCards(self), game, Culture.DUNLAND).size()>=3) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, Filters.any));
            return Collections.singletonList(action);
        }
        return null;
    }
}
