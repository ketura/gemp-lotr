package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Gandalf, Wisest of the Istari
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion - Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 8
 * Card Number: 1R70
 * Game Text: Each time you play a [GANDALF] spell, you may draw a card.
 */
public class Card40_070 extends AbstractCompanion{
    public Card40_070() {
        super(4, 7, 4, 8, Culture.GANDALF, Race.WIZARD, null, "Gandalf",
                "Wisest of the Istari", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Culture.GANDALF, Keyword.SPELL)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
