package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * Threat of Mordor
 * Event • Skirmish
 * Make a [Sauron] minion strength +1 for each threat.
 * http://lotrtcg.org/coreset/sauron/threatofmordor(r1).png
 */
public class Card20_375 extends AbstractEvent {
    public Card20_375() {
        super(Side.SHADOW, 1, Culture.SAURON, "Threat of Mordor", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ForEachThreatEvaluator(), Culture.SAURON, CardType.MINION));
        return action;
    }
}
