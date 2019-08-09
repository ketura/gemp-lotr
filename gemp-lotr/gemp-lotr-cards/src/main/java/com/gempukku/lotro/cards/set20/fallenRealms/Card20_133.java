package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachTwilightEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.LimitEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 2
 * Red Rage
 * Fallen Realms	Event • Skirmish
 * Make a Southron strength +1 for each twilight token (limit +6).
 */
public class Card20_133 extends AbstractEvent {
    public Card20_133() {
        super(Side.SHADOW, 2, Culture.FALLEN_REALMS, "Red Rage", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new LimitEvaluator(new ForEachTwilightEvaluator(), 6), Keyword.SOUTHRON));
        return action;
    }
}
