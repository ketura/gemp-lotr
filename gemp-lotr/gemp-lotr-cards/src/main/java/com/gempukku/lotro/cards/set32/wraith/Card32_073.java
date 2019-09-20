package com.gempukku.lotro.cards.set32.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. The site number of each [WRAITH] minion is -3.
 * While you can spot a follower attached to a companion, each [WRAITH] minion is strength +1.
 */
public class Card32_073 extends AbstractMinion {
    public Card32_073() {
        super(4, 9, 3, 5, Race.NAZGUL, Culture.WRAITH, Names.otsea, "Revived", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new MinionSiteNumberModifier(self, Culture.WRAITH, null, -3));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.WRAITH, CardType.MINION),
                        new SpotCondition(CardType.FOLLOWER, Filters.attachedTo(CardType.COMPANION)), 1));
        return modifiers;
    }
}
