package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
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
 * Title: Ranger of the North
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion - Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Card Number: 1C114
 * Game Text: Ranger.
 * While you can spot a [GONDOR] man, this companion's twilight cost is -1 in your starting fellowship.
 * At the start of each skirmish involving this companion, each minion skirmishing this companion must exert if
 * at a site from your adventure deck.
 */
public class Card40_114 extends AbstractCompanion {
    public Card40_114() {
        super(2, 6, 3, 6, Culture.GONDOR, Race.MAN, null, "Ranger of the North");
        addKeyword(Keyword.RANGER);
    }


    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP
                && Filters.canSpot(gameState, modifiersQuerying, Culture.GONDOR, Race.MAN))
            return -1;
        return 0;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, self, Filters.inSkirmish)
                && PlayConditions.location(game, Filters.owner(self.getOwner()))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, CardType.MINION, Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }
}
