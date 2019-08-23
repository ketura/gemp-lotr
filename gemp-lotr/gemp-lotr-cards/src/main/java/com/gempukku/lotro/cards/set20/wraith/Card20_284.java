package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.GameHasCondition;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 3
 * •Black Beast of Angmar, Death in Flight
 * Ringwraith	Possession • Mount
 * 2	1
 * Bearer must be The Witch-king.
 * While the Ring-bearer has 6 or less resistance, The Witch-king may not take wounds.
 */
public class Card20_284 extends AbstractAttachable {
    public Card20_284() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.WRAITH, PossessionClass.MOUNT, "Black Beast of Angmar", "Death in Flight", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.witchKing;
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public int getVitality() {
        return 1;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new CantTakeWoundsModifier(self,
                new GameHasCondition(Filters.ringBearer, Filters.maxResistance(6)), Filters.witchKing));
        return modifiers;
    }
}
