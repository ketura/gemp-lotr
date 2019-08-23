package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;

/**
 * 2	Ransacked
 * Dunland	Event • Skirmish
 * Make a [Dunland] Man strength +1 for each possession you can spot.
 */
public class Card20_023 extends AbstractEvent {
    public Card20_023() {
        super(Side.SHADOW, 2, Culture.DUNLAND, "Ransacked", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CountSpottableEvaluator(CardType.POSSESSION), Culture.DUNLAND, Race.MAN));
        return action;
    }
}
