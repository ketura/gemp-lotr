package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 [ORC] minions. At the start of your Shadow phase, you may discard an [ORC] condition from
 * play to play an [ORC] minion. It is twilight cost -2 (or -4 if you can spot 6 or more companions).
 */
public class Card15_097 extends AbstractPermanent {
    public Card15_097() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "Beasts of Burden");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Culture.ORC, CardType.MINION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SHADOW)
                && ((StartOfPhaseResult) effectResult).getPlayerId().equals(playerId)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.ORC, CardType.CONDITION)
                && PlayConditions.canPlayFromHand(playerId, game, PlayConditions.canSpot(game, 6, CardType.COMPANION) ? -4 : -2, Culture.ORC, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.ORC, CardType.CONDITION));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, PlayConditions.canSpot(game, 6, CardType.COMPANION) ? -4 : -2, Culture.ORC, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
