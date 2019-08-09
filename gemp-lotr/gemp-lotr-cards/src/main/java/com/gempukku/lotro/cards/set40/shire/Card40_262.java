package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PlayNextSiteEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Pippin, Halfling Trickster
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion - Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Card Number: 1R262
 * Game Text: When you play Pippin, you may play the Fellowship's next site (replacing opponent's site if necessary).
 */
public class Card40_262 extends AbstractCompanion {
    public Card40_262() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Pippin",
                "Halfling Trickster", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new PlayNextSiteEffect(action, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
