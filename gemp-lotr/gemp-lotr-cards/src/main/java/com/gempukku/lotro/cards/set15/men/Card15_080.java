package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Possession • Hand weapon
 * Strength: +2
 * Game Text: Bearer must be a [MEN] Man. While bearer is skirmishing a hunter companion, bearer is strength +2.
 */
public class Card15_080 extends AbstractAttachable {
    public Card15_080() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.MEN, PossessionClass.HAND_WEAPON, "Great Axe");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MEN, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), new SpotCondition(CardType.COMPANION, Keyword.HUNTER, Filters.inSkirmishAgainst(Filters.hasAttached(self))), 2));
        return modifiers;
    }
}
