package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Play a [MEN] minion stacked on a [MEN] possession as if from hand.
 */
public class Card17_046 extends AbstractEvent {
    public Card17_046() {
        super(Side.SHADOW, 0, Culture.MEN, "Pandemonium", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canPlayFromStacked(self.getOwner(), game, Filters.and(Culture.MEN, CardType.POSSESSION), Culture.MEN, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromStackedEffect(playerId, Filters.and(Culture.MEN, CardType.POSSESSION), Culture.MEN, CardType.MINION));
        return action;
    }
}
