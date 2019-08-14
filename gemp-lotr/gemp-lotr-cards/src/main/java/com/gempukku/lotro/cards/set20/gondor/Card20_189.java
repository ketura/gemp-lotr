package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0
 * Familiar Ground
 * Gondor	Event • Skirmish
 * Exert a [Gondor] ranger to make a roaming minion strength -2 (or strength -4 if at a site from your adventure deck).
 */
public class Card20_189 extends AbstractEvent {
    public Card20_189() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Familiar Ground", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.GONDOR, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId,  1, 1, Culture.GONDOR, Keyword.RANGER));
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ConditionEvaluator(-2, -4, new LocationCondition(Filters.owner(playerId))), CardType.MINION, Keyword.ROAMING));
        return action;
    }
}
