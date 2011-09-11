package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a Nazgul to wound every archer companion.
 */
public class Card1_223 extends AbstractEvent {
    public Card1_223() {
        super(Side.SHADOW, Culture.WRAITH, "Relentless Charge", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose a Nazgul", true, Filters.race(Race.NAZGUL), Filters.canExert()));
        List<PhysicalCard> archers = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION), Filters.keyword(Keyword.ARCHER));
        for (PhysicalCard archer : archers)
            action.addEffect(new WoundCharacterEffect(archer));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
