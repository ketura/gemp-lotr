package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Knight. At the start of each fellowship phase, you may add (2) to heal another [GONDOR] companion.
 */
public class Card7_081 extends AbstractCompanion {
    public Card7_081() {
        super(4, 8, 4, Culture.GONDOR, Race.MAN, Signet.ARAGORN, "Aragorn", true);
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 2));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, CardType.COMPANION, Filters.not(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
