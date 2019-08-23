package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;

/**
 * 2
 * Overwhelming Numbers
 * Fallen Realms	Event • Skirmish
 * Make an Easterling strength +1 for each Easterling you can spot.
 */
public class Card20_132 extends AbstractEvent {
    public Card20_132() {
        super(Side.SHADOW, 2, Culture.FALLEN_REALMS, "Overwhelming Numbers", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CountSpottableEvaluator(Keyword.EASTERLING), Keyword.EASTERLING));
        return action;
    }
}
