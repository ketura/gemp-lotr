package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;

/**
 * 2
 * Crippling Arrows
 * Elven	Event • Maneuver
 * Each wounded minion is prevented from being fierce until the regroup phase.
 */
public class Card20_079 extends AbstractEvent {
    public Card20_079() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Crippling Arrows", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        Collection<PhysicalCard> woundedMinions = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), CardType.MINION, Filters.wounded);
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new RemoveKeywordModifier(self, Filters.in(woundedMinions), Keyword.FIERCE), Phase.REGROUP));
                    }
                });
        return action;
    }
}
