package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [MEN] minion strength +2 (or +4 if that minion is at a battleground or plains site).
 */
public class Card11_092 extends AbstractEvent {
    public Card11_092() {
        super(Side.SHADOW, 1, Culture.MEN, "Overrun", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new ConditionEvaluator(2, 4, new LocationCondition(Filters.or(Keyword.BATTLEGROUND, Keyword.PLAINS))), Culture.MEN, CardType.MINION));
        return action;
    }
}
