package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardFromHandResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * The Long Dark of Moria
 * Moria	Condition • Support Area
 * Each unique [Moria] minion is strength +1.
 * Response: Each time you discard a Goblin from hand during the shadow phase, you may stack that minion here.
 */
public class Card20_279 extends AbstractPermanent {
    public Card20_279() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "The Long Dark of Moria");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Culture.MORIA, CardType.MINION, Filters.unique), 1);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromHand(game, effectResult, Filters.owner(playerId), Race.GOBLIN)
                && PlayConditions.isPhase(game, Phase.SHADOW)) {
            DiscardCardFromHandResult discardResult = (DiscardCardFromHandResult) effectResult;
            PhysicalCard discardedCard = discardResult.getDiscardedCard();

            ActivateCardAction action = new ActivateCardAction(self);
            action.setText("Stack "+ GameUtils.getFullName(discardedCard));
            action.appendEffect(
                    new StackCardFromDiscardEffect(discardedCard, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
