package com.gempukku.lotro.cards.set40.isengard;

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
 * Title: The Hour Grows Late
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event - Maneuver
 * Card Number: 1U141
 * Game Text: Spell. Spot Saruman and an exhausted companion to add a threat for each wound on that companion.
 */
public class Card40_141 extends AbstractEvent {
    public Card40_141() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "The Hour Grows Late", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.saruman)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.exhausted);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose exhausted companion", CardType.COMPANION, Filters.exhausted) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        final int wounds = game.getGameState().getWounds(card);
                        action.appendEffect(
                                new AddThreatsEffect(playerId, self, wounds));
                    }
                });
        return action;
    }
}
