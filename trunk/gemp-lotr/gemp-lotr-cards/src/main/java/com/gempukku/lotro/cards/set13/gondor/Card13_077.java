package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 5
 * Game Text: At the start of each fellowship phase, you may add (2) to reinforce a [GONDOR] token.
 */
public class Card13_077 extends AbstractCompanion {
    public Card13_077() {
        super(2, 6, 3, 5, Culture.GONDOR, Race.MAN, null, "Tradesman From Lebennin");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 2));
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.GONDOR));
            return Collections.singletonList(action);
        }
        return null;
    }
}
