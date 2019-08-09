package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.RemoveGameTextModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.GameHasCondition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * •The Balrog's Sword, Blade of Morgoth
 * Moria	Artifact • Hand Weapon
 * 2
 * Bearer must be the Balrog. It is damage +1.
 * The game text of Free Peoples armor possessions and Free Peoples artifacts does not apply during skirmishes involving the Balrog.
 */
public class Card20_255 extends AbstractAttachable {
    public Card20_255() {
        super(Side.SHADOW, CardType.ARTIFACT, 1, Culture.MORIA, PossessionClass.HAND_WEAPON, "The Balrog's Sword", "Blade of Morgoth", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.balrog;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
        modifiers.add(
                new RemoveGameTextModifier(self, new GameHasCondition(Filters.balrog, Filters.inSkirmish),
                        Filters.and(Side.FREE_PEOPLE, Filters.or(Filters.and(CardType.POSSESSION, PossessionClass.ARMOR), CardType.ARTIFACT))));
        return modifiers;
    }
}
