package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 13
 * Vitality: 3
 * Site: 4
 * Game Text: To play, spot 2 [MEN] Men. While this minion bears a possession, he is strength +2 for each Free Peoples
 * culture you can spot.
 */
public class Card17_061 extends AbstractMinion {
    public Card17_061() {
        super(6, 13, 3, 4, Race.MAN, Culture.MEN, "Sunland Weaponmaster");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Culture.MEN, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, new SpotCondition(CardType.POSSESSION, Filters.attachedTo(self)),
new MultiplyEvaluator(2, new CountCulturesEvaluator(Side.FREE_PEOPLE))));
}
}
