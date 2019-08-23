package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * ❶ Weapons of the East [Fal]
 * Condition • Support Area
 * To play, spot an Easterling.
 * Each time a [Fal] possession is discarded, you may spot an Easterling stack it here instead.
 * Shadow: Remove ❷ to play a [Fal] possession stacked here as if from hand.
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/weaponsoftheeast(r3).jpg
 */
public class Card20_485 extends AbstractPermanent {
    public Card20_485() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.FALLEN_REALMS, "Weapons of the East");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Keyword.EASTERLING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Culture.FALLEN_REALMS, CardType.POSSESSION)
                && PlayConditions.canSpot(game, Keyword.EASTERLING)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new StackCardFromDiscardEffect(((DiscardCardsFromPlayResult) effectResult).getDiscardedCard(), self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 2)
                && PlayConditions.canPlayFromStacked(playerId, game, 2, self, Culture.FALLEN_REALMS, CardType.POSSESSION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, Culture.FALLEN_REALMS, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
