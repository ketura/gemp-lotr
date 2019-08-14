package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * 1
 * Memories of Khazad-dum
 * Moria	Event • Skirmish
 * Make a [Moria] Goblin strength +2 (or strength +3 if you can spot a Dwarf).
 */
public class Card20_277 extends AbstractEvent {
    public Card20_277() {
        super(Side.SHADOW, 1, Culture.MORIA, "Memories of Moria", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ConditionEvaluator(2, 3, new SpotCondition(Race.DWARF)), Culture.MORIA, Race.GOBLIN));
        return action;
    }
}
