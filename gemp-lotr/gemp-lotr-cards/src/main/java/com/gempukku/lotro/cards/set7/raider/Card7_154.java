package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.condition.InitiativeCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make a [RAIDER] Man strength +3 (or +6 if you have initiative).
 */
public class Card7_154 extends AbstractEvent {
    public Card7_154() {
        super(Side.SHADOW, 2, Culture.RAIDER, "New Strength Came Now", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, playerId, new ConditionEvaluator(3, 6, new InitiativeCondition(Side.SHADOW)), Culture.RAIDER, Race.MAN));
        return action;
    }
}
