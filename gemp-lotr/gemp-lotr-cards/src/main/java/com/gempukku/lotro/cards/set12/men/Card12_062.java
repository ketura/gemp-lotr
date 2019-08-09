package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: While this minion is exhausted, each unbound companion is resistance -1 (or -2 if the fellowship is at
 * a battleground site).
 */
public class Card12_062 extends AbstractMinion {
    public Card12_062() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Dunlending Zealot");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ResistanceModifier(self, Filters.unboundCompanion, new SpotCondition(self, Filters.exhausted),
new ConditionEvaluator(-1, -2, new LocationCondition(Keyword.BATTLEGROUND))));
}
}
