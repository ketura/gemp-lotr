package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Exert your [MORIA] Orc to draw 2 cards.
 */
public class Card1_197 extends AbstractEvent {
    public Card1_197() {
        super(Side.SHADOW, Culture.MORIA, "Threat of the Unknown", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose a MORIA Orc", true, Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC), Filters.canExert()));
        action.addEffect(
                new DrawCardEffect(playerId));
        action.addEffect(
                new DrawCardEffect(playerId));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
