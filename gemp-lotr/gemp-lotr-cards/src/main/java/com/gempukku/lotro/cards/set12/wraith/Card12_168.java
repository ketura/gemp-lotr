package com.gempukku.lotro.cards.set12.wraith;
import java.util.List;
import java.util.Collections;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be a Nazgul. While bearer is at a battleground or forest site, he is strength +1.
 */
public class Card12_168 extends AbstractAttachable {
    public Card12_168() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.WRAITH, PossessionClass.HAND_WEAPON, "Nazgul Blade");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.hasAttached(self), null,
new ConditionEvaluator(1, 2,
new LocationCondition(Filters.or(Keyword.BATTLEGROUND, Keyword.FOREST)))));
}
}
