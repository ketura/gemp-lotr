package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot Frodo, Smeagol is resistance +1. While you can spot Sam, Smeagol is strength +1.
 * While you can spot Bilbo, Smeagol is damage +1.
 */
public class Card15_044 extends AbstractPermanent {
    public Card15_044() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Herbs and Stewed Rabbit");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ResistanceModifier(self, Filters.smeagol, new SpotCondition(Filters.frodo), 1));
        modifiers.add(
                new StrengthModifier(self, Filters.smeagol, new SpotCondition(Filters.sam), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.smeagol, new SpotCondition(Filters.name("Bilbo")), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
