package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * 1
 * Halfling Dexterity
 * Shire	Event • Skirmish
 * Make a Hobbit strength +2 (or strength +4 if skirmishing more than one minion).
 */
public class Card20_475 extends AbstractEvent {
    public Card20_475() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Halfling Dexterity", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                if (Filters.inSkirmish.accepts(gameState, modifiersQuerying, cardAffected) &&
                                        gameState.getSkirmish().getShadowCharacters().size()>1)
                                    return 4;
                                return 2;
                            }
                        }, Race.HOBBIT));
        return action;
    }
}
