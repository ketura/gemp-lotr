package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.CantExertWithCardModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot a [MORIA] Orc. Plays on a Hobbit companion. Bearer may be exerted only by Shadow cards.
 */
public class Card2_071 extends AbstractAttachable {
    public Card2_071() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.MORIA, null, "Throw Yourself in Next Time");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.MORIA, Race.ORC);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.HOBBIT, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new CantExertWithCardModifier(self, Filters.hasAttached(self), Filters.not(Side.SHADOW)));
    }
}
