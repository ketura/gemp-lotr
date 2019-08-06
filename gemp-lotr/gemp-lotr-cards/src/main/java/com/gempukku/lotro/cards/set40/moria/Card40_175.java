package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Title: Goblin Swarms
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Condition - Support Area
 * Card Number: 1R175
 * Game Text: Response: If a [MORIA] Goblin wins a skirmish, stack that Goblin here.
 * Shadow: Play a Goblin stacked here as if from hand.
 */
public class Card40_175 extends AbstractPermanent {
    public Card40_175() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Goblin Swarms");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canPlayFromStacked(playerId, game, self, Race.GOBLIN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.and(Culture.MORIA, Race.GOBLIN))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Culture.MORIA, Race.GOBLIN, Filters.inSkirmish)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndStackCardFromPlayEffect(action, playerId, self, Culture.MORIA, Race.GOBLIN, Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }

}
