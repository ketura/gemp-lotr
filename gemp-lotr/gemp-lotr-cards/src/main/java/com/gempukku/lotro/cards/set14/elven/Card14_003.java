package com.gempukku.lotro.cards.set14.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Expanded Middle-earth
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Companion • Elf
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Elladan is twilight cost -2. Each time you play an [ELVEN] event during a skirmish involving Elrohir
 * or Elladan, each minion in that skirmish is strength -2.
 */
public class Card14_003 extends AbstractCompanion {
    public Card14_003() {
        super(3, 7, 3, 6, Culture.ELVEN, Race.ELF, null, "Elrohir", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.name("Elladan"), -2);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Culture.ELVEN, CardType.EVENT)
                && PlayConditions.canSpot(game, Filters.or(Filters.name("Elrohir"), Filters.name("Elladan")), Filters.inSkirmish)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmish), -2), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
