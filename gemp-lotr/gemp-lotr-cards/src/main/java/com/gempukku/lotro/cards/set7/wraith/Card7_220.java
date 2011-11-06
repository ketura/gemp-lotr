package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [WRAITH] Orc strength +2 (or +4 if you spot 2 wounds on the Ring-bearer).
 */
public class Card7_220 extends AbstractEvent {
    public Card7_220() {
        super(Side.SHADOW, 1, Culture.WRAITH, "War Long Planned", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ConditionEvaluator(2, 4,
                                new SpotCondition(Keyword.RING_BEARER, Filters.hasWounds(2))),
                        Culture.WRAITH, Race.ORC));
        return action;
    }
}
