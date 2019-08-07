package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Title: Threat of Mordor
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event - Skirmish
 * Card Number: 1C241
 * Game Text: Make a [SAURON] minion strength +1 for each threat.
 */
public class Card40_241 extends AbstractEvent {
    public Card40_241() {
        super(Side.SHADOW, 1, Culture.SAURON, "Threat of Mordor", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ForEachThreatEvaluator(), Culture.SAURON, CardType.MINION));
        return action;
    }
}
