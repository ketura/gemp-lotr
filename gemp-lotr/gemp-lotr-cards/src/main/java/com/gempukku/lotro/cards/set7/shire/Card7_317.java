package com.gempukku.lotro.cards.set7.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Aragorn
 * Game Text: Ring-bearer (resistance 10). Ring-bound. Each time the fellowship moves, you may add (3) to remove
 * a threat.
 */
public class Card7_317 extends AbstractCompanion {
    public Card7_317() {
        super(0, 3, 4, 10, Culture.SHIRE, Race.HOBBIT, Signet.ARAGORN, "Frodo", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 3));
            action.appendEffect(
                    new RemoveThreatsEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
