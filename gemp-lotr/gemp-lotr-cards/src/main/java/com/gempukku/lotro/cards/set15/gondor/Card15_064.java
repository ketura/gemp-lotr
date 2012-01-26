package com.gempukku.lotro.cards.set15.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: Ranger. Hunter 1. While you can spot 2 [GONDOR] rangers, Madril is twilight cost -2. At the start of
 * the maneuver phase, each minion is site number +1 for each threat you can spot until the start of the regroup phase.
 */
public class Card15_064 extends AbstractCompanion {
    public Card15_064() {
        super(2, 5, 3, 6, Culture.GONDOR, Race.MAN, null, "Madril", true);
        addKeyword(Keyword.RANGER);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, 2, Culture.GONDOR, Keyword.RANGER))
            return -2;
        return 0;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            final int threats = game.getGameState().getThreats();
            if (threats > 0)
                action.appendEffect(
                        new AddUntilStartOfPhaseModifierEffect(
                                new MinionSiteNumberModifier(self, CardType.MINION, null, threats), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
