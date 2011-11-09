package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.HasInitiativeModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.MinThreatCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 6
 * Game Text: While you can spot a threat and another [SAURON] Orc, the Shadow has initiative, regardless of the Free
 * Peoples player's hand.
 */
public class Card7_286 extends AbstractMinion {
    public Card7_286() {
        super(2, 7, 2, 6, Race.ORC, Culture.SAURON, "Mordor Fighter");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new HasInitiativeModifier(self,
                        new AndCondition(new MinThreatCondition(1), new SpotCondition(Filters.not(self), Culture.SAURON, Race.ORC)),
                        Side.SHADOW));
    }
}
