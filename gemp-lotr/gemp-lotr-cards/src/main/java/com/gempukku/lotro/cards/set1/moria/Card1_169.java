package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.Collections;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Assignment: Spot 5 [MORIA] minions to make the Free Peoples player assign the Ring-bearer to a skirmish.
 */
public class Card1_169 extends AbstractEvent {
    public Card1_169() {
        super(Side.SHADOW, Culture.MORIA, "The End Comes", Phase.ASSIGNMENT);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.type(CardType.MINION)) >= 5;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        final PhysicalCard ringBearer = game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId());

        if (!isFPCharacterAssigned(game.getGameState(), ringBearer)) {
            action.addEffect(
                    new ChooseActiveCardEffect(game.getGameState().getCurrentPlayerId(), "Choose minion to assign Ring-Bearer to", Filters.type(CardType.MINION), Filters.notAssigned()) {
                        @Override
                        protected void cardSelected(PhysicalCard minion) {
                            action.addEffect(
                                    new AssignmentEffect(ringBearer, Collections.singletonList(minion), "The End Comes effect"));
                        }
                    });
        }
        return action;
    }

    private boolean isFPCharacterAssigned(GameState gameState, PhysicalCard card) {
        for (Skirmish skirmish : gameState.getAssignments()) {
            if (skirmish.getFellowshipCharacter() == card)
                return true;
        }
        return false;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
