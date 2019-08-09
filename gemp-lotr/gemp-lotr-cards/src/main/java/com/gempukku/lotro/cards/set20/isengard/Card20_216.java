package com.gempukku.lotro.cards.set20.isengard;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Vile Blood
 * Isengard	Condition • Minion
 * +1
 * Bearer must be an [Isengard] orc. While you have initiative, you may play this card from your discard pile.
 */
public class Card20_216 extends AbstractAttachable {
    public Card20_216() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.ISENGARD, null, "Vile Blood", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ISENGARD, Race.ORC);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new VitalityModifier(self, Filters.hasAttached(self), 1));
}


    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SHADOW)
                && PlayConditions.hasInitiative(game, Side.SHADOW)
                && PlayConditions.canPlayFromDiscard(playerId, game, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0, false));
        }
        return null;
    }
}
