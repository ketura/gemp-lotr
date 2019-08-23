package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.condition.FierceSkirmishCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;

/**
 * Title: Sentinels of Numenor
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1C120
 * Game Text: Make a [GONDOR] companion strength +2 or make a [GONDOR] companion strength +4 if that companion is in
 * a fierce skirmish.
 */
public class Card40_120 extends AbstractEvent {
    public Card40_120() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Sentinels of Numenor", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new ConditionEvaluator(2, 4, new FierceSkirmishCondition()),
                        Culture.GONDOR, CardType.COMPANION));
        return action;
    }
}
