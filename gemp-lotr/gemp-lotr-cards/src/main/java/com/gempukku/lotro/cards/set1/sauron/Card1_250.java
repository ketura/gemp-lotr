package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a [SAURON] Orc to wound a companion (except the Ring-bearer).
 */
public class Card1_250 extends AbstractEvent {
    public Card1_250() {
        super(Side.SHADOW, Culture.SAURON, "Hate", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose SAURON Orc", true, Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC), Filters.canExert()));
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose a companion (except the Ring-Bearer)", Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER))) {
                    @Override
                    protected void cardSelected(PhysicalCard companion) {
                        action.addEffect(
                                new WoundCharacterEffect(companion));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
