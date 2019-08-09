package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Orc Skulker
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion - Orc
 * Strength: 9
 * Vitality: 3
 * Home: 6
 * Card Number: 1U235
 * Game Text: Tracker.
 * While you can spot 3 trackers, the Free Peoples player may not play skirmish events or use skirmish special abilities
 * in skirmishes involving this minion.
 */
public class Card40_235 extends AbstractMinion {
    public Card40_235() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Orc Skulker", null, true);
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier modifier = new SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(
                self, new AndCondition(new SpotCondition(3, Keyword.TRACKER), new SpotCondition(self, Filters.inSkirmish)),
                Side.FREE_PEOPLE, Phase.SKIRMISH);
        return Collections.singletonList(modifier);
    }
}
