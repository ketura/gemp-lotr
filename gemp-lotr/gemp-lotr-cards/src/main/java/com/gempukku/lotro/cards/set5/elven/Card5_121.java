package com.gempukku.lotro.cards.set5.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Archer. Each time Legolas wins a skirmish, you may heal a Dwarf companion or another Elf companion.
 */
public class Card5_121 extends AbstractCompanion {
    public Card5_121() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, Signet.THÉODEN, "Legolas", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(self), Filters.or(Race.DWARF, Race.ELF)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
