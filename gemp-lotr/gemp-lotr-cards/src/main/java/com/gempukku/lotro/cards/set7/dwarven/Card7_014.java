package com.gempukku.lotro.cards.set7.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DiscardStackedCardsEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: Choose one: Spot a Dwarf to draw a card; or, if this card is stacked on a [DWARVEN] condition, spot
 * a Dwarf companion and discard this event to exert a minion twice.
 */
public class Card7_014 extends AbstractEvent {
    public Card7_014() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Slaked Thirsts", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new DrawCardsEffect(action, playerId, 1));
        return action;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseStackedFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.stackedOn(self, game, Culture.DWARVEN, CardType.CONDITION)
                && PlayConditions.canSpot(game, Race.DWARF, CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardStackedCardsEffect(self, self));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
