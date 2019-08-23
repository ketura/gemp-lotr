package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;
import com.gempukku.lotro.logic.modifiers.condition.SpotCulturesCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;

/**
 * Title: War Cry of Mordor
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event - Skirmish
 * Card Number: 1C242
 * Game Text: Make a [SAURON] minion strength +2 (or strength +3 if you cannot spot more than 2 Free Peoples cultures.)
 */
public class Card40_242 extends AbstractEvent {
    public Card40_242() {
        super(Side.SHADOW, 1, Culture.SAURON, "War Cry of Mordor", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ConditionEvaluator(2, 3, new NotCondition(new SpotCulturesCondition(3, Side.FREE_PEOPLE))), Culture.SAURON, CardType.MINION));
        return action;
    }
}
