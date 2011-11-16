package com.gempukku.lotro.cards.set10.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Each site on the adventure path is a battleground. While you have initiative, each Uruk-Hai
 * is strength +1.
 */
public class Card10_035 extends AbstractPermanent {
    public Card10_035() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Suffered Much Loss");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(CardType.SITE, Zone.ADVENTURE_PATH), Keyword.BATTLEGROUND));
        modifiers.add(
                new StrengthModifier(self, Race.URUK_HAI, new InitiativeCondition(Side.SHADOW), 1));
        return modifiers;
    }
}
