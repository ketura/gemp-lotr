package com.gempukku.lotro.cards.set32.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Artifact
 * Vitality: +1
 * Game Text: Bearer must be Gandalf. The twilight cost of each Gandalf event is -1. Response: If Gandalf
 * is about to be discarded, exert him to prevent that.
 */
public class Card32_023 extends AbstractAttachableFPPossession {
    public Card32_023() {
        super(0, 0, 1, Culture.GANDALF, CardType.ARTIFACT, PossessionClass.RING, "Narya", null, true);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }
    
    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new TwilightCostModifier(self, Filters.and(Culture.GANDALF, CardType.EVENT), -1));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingDiscarded(effect, game, Filters.gandalf)
                && PlayConditions.canExert(self, game, Filters.gandalf)) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
            Collection<PhysicalCard> cards = Filters.filter(discardEffect.getAffectedCardsMinusPrevented(game), game, Filters.gandalf);
                for (PhysicalCard card : cards)
                    action.appendEffect(
new PreventCardEffect(discardEffect, card));

                return Collections.singletonList(action);
        }
        return null;
    }
}
