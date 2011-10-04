package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Exert a Dwarf to make an opponent draw 2 cards. That player then chooses to either discard
 * 2 Shadow cards from hand or skip his or her next Shadow phase.
 */
public class Card2_015 extends AbstractEvent {
    public Card2_015() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "What Are We Waiting For?", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.DWARF)));
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(final String opponentId) {
                        action.insertEffect(new DrawCardEffect(opponentId, 2));
                        List<Effect> possibleEffects = new LinkedList<Effect>();
                        possibleEffects.add(
                                new ChooseAndDiscardCardsFromHandEffect(action, opponentId, 2, 2, Filters.side(Side.SHADOW)));
                        possibleEffects.add(
                                new SkipNextShadowPhaseChooseableEffect(self, opponentId));
                        action.appendEffect(
                                new ChoiceEffect(action, opponentId, possibleEffects));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    private class SkipNextShadowPhaseChooseableEffect extends AddUntilEndOfTurnModifierEffect {
        public SkipNextShadowPhaseChooseableEffect(PhysicalCard card, final String opponentId) {
            super(
                    new AbstractModifier(card, "Skip Shadow phase", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
                        @Override
                        public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId, boolean result) {
                            return phase == Phase.SHADOW && opponentId.equals(playerId);
                        }
                    }
            );
        }

        @Override
        public String getText(LotroGame game) {
            return "Skip your Shadow phase";
        }
    }
}
