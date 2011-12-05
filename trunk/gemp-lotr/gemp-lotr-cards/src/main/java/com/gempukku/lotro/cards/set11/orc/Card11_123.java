package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, spot an [ORC] minion. Each time the fellowship moves from an underground site, you may take
 * an [ORC] minion from your discard pile into hand.
 */
public class Card11_123 extends AbstractPermanent {
    public Card11_123() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "Goblin Hordes");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, Keyword.UNDERGROUND)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.ORC, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
