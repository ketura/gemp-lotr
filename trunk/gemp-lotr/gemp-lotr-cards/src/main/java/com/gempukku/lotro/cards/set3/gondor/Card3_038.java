package com.gempukku.lotro.cards.set3.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Ranger. Each time the fellowship moves during the fellowship phase, remove (2).
 */
public class Card3_038 extends AbstractCompanion {
    public Card3_038() {
        super(4, 8, 4, 6, Culture.GONDOR, Race.MAN, Signet.FRODO, "Aragorn", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES
                && game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new RemoveTwilightEffect(2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
