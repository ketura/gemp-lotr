package com.gempukku.lotro.cards.set5.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Damage +1. Each time Gimli wins a skirmish, you may wound a minion assigned to skirmish
 * an unbound companion.
 */
public class Card5_007 extends AbstractCompanion {
    public Card5_007() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, Signet.THÉODEN, "Gimli", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.assignedAgainst(Filters.unboundCompanion)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
