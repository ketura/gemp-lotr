package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * 3
 * Hillman Warrior
 * Dunland	Minion • Man
 * 9	1	3
 * While a [Dunland] Man is stacked on a site you control, this minion is strength +3, damage +1 and may only
 * take wounds during skirmishes.
 */
public class Card20_019 extends AbstractMinion {
    public Card20_019() {
        super(3, 9, 1, 3, Race.MAN, Culture.DUNLAND, "Hillman Warrior");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        SpotCondition condition = new SpotCondition(Filters.siteControlled(self.getOwner()), Filters.hasStacked(Culture.DUNLAND, Race.MAN));
        modifiers.add(
                new StrengthModifier(self, self, condition, 3));
        modifiers.add(
                new KeywordModifier(self, self, condition, Keyword.DAMAGE, 1));
        modifiers.add(
                new CantTakeWoundsModifier(self, new AndCondition(condition, new NotCondition(new PhaseCondition(Phase.SKIRMISH))), self));
        return modifiers;
    }
}
