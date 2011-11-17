package com.gempukku.lotro.cards.set10.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [WRAITH] Orc strength +2 for each wound on the Ring-bearer.
 */
public class Card10_066 extends AbstractEvent {
    public Card10_066() {
        super(Side.SHADOW, 1, Culture.WRAITH, "Ten Times Outnumbered", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return 2 * gameState.getWounds(gameState.getRingBearer(gameState.getCurrentPlayerId()));
                            }
                        }, Culture.WRAITH, Race.ORC));
        return action;
    }
}
