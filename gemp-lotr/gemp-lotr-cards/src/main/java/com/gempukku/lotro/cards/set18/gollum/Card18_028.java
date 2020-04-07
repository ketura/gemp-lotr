package com.gempukku.lotro.cards.set18.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Spot Shelob to make a minion strength +1 for each threat you can spot.
 */
public class Card18_028 extends AbstractEvent {
    public Card18_028() {
        super(Side.SHADOW, 2, Culture.GOLLUM, "Countless Cords", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.name("Shelob"));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new ForEachThreatEvaluator(), CardType.MINION));
        return action;
    }
}
