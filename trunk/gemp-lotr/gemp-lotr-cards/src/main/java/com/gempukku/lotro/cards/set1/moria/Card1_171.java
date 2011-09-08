package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.Collections;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Assignment: Assign an exhausted companion (except the Ring-bearer) to skirmish a [MORIA] Orc.
 */
public class Card1_171 extends AbstractEvent {
    public Card1_171() {
        super(Side.SHADOW, Culture.MORIA, "Frenzy", Phase.ASSIGNMENT);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose exhausted companion", Filters.type(CardType.COMPANION), Filters.not(Filters.canExert()), Filters.notAssigned()) {
                    @Override
                    protected void cardSelected(final PhysicalCard companion) {
                        action.addEffect(
                                new ChooseActiveCardEffect(playerId, "Choose MORIA Orc", Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC), Filters.notAssigned()) {
                                    @Override
                                    protected void cardSelected(PhysicalCard moriaOrc) {
                                        game.getGameState().assignToSkirmishes(companion, Collections.singletonList(moriaOrc));
                                    }
                                });
                    }
                }
        );
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
