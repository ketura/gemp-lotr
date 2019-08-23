package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 2
 * The Hour Grows Late
 * Isengard	Event • Maneuver
 * Spell.
 * Spot Saruman and an exhausted companion to add a threat for each wound on that companion.
 */
public class Card20_248 extends AbstractEvent {
    public Card20_248() {
        super(Side.SHADOW, 2, Culture.ISENGARD, "The Hour Grows Late", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.saruman)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.exhausted);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action= new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose an exhausted companion", CardType.COMPANION, Filters.exhausted) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int wounds = game.getGameState().getWounds(card);
                        action.appendEffect(
                                new AddThreatsEffect(playerId, self, wounds));
                    }
                });
        return action;
    }
}
