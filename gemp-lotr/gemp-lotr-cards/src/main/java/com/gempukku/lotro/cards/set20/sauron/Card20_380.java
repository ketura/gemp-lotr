package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.condition.CanSpotFPCulturesCondition;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * War Cry of Morannon
 * Event • Skirmish
 * Make a [Sauron] minion strength +3 (or strength +4 if you cannot spot 3 Free Peoples cultures).
 * http://lotrtcg.org/coreset/sauron/warcryofmorannon(r1).png
 */
public class Card20_380 extends AbstractEvent {
    public Card20_380() {
        super(Side.SHADOW, 1, Culture.SAURON, "War Cry of Morannon", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new ConditionEvaluator(2, 3,
                        new NotCondition(new CanSpotFPCulturesCondition(self.getOwner(), 3))), Culture.SAURON, CardType.MINION));
        return action;
    }
}
