package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.HashSet;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Reveal your hand to make a [WRAITH] minion strength +1 for each [WRAITH] minion in your hand.
 */
public class Card11_215 extends AbstractEvent {
    public Card11_215() {
        super(Side.SHADOW, 0, Culture.WRAITH, "Riders in Black", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new RevealCardsFromHandEffect(self, playerId, new HashSet<PhysicalCard>(game.getGameState().getHand(playerId))));
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return Filters.filter(gameState.getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.WRAITH, CardType.MINION).size();
                            }
                        }, Culture.WRAITH, CardType.MINION));
        return action;
    }
}
