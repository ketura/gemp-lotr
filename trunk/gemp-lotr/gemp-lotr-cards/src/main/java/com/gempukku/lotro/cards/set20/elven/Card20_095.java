package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * Might of the Last Homely House
 * Elven	Event • Skirmish
 * Make a minion strength -X where X is the number of Rivendell allies you can spot.
 */
public class Card20_095 extends AbstractEvent {
    public Card20_095() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Might of the Last Homely House", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new MultiplyEvaluator(-1, new CountActiveEvaluator(CardType.ALLY, Keyword.RIVENDELL)), CardType.MINION));
        return action;
    }
}
