package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Dunlending Veteran
 * Dunland	Minion • Man
 * 6	1	3
 * Dunlending Veteran is strength +1 for each Free Peoples possession you can spot.
 */
public class Card20_007 extends AbstractMinion {
    public Card20_007() {
        super(1, 6, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Veteran");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, null, new CountActiveEvaluator(Side.FREE_PEOPLE, CardType.POSSESSION)));
        return modifiers;
    }
}
