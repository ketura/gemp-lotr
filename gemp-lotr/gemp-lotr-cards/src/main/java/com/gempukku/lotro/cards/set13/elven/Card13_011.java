package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 7
 * Game Text: While the fellowship is at a forest site, each Elf who has resistance 7 or more gains muster. Each time
 * the fellowship moves during the regroup phase, you may take an [ELVEN] event into hand from your discard pile
 */
public class Card13_011 extends AbstractCompanion {
    public Card13_011() {
        super(2, 6, 3, 7, Culture.ELVEN, Race.ELF, null, "Celeborn", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, Filters.and(Race.ELF, Filters.minResistance(7)), new LocationCondition(Keyword.FOREST), Keyword.MUSTER, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult) && PlayConditions.isPhase(game, Phase.REGROUP)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.ELVEN, CardType.EVENT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
