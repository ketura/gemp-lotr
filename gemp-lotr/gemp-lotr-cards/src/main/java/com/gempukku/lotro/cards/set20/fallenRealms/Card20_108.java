package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * ❶ Called to Mordor [Fal]
 * Event • Skirmish
 * Make an Easterling strength +1 for each wound on each character in his skirmish.
 * http://lotrtcg.org/coreset/fallenrealms/calledtomordor(r3).jpg
 */
public class Card20_108 extends AbstractEvent {
    public Card20_108() {
        super(Side.SHADOW, 1, Culture.FALLEN_REALMS, "Called to Mordor", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                int wounds = 0;
                                if (Filters.inSkirmish.accepts(gameState, modifiersQuerying, cardAffected)) {
                                    for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Filters.character, Filters.inSkirmish, Filters.wounded)) {
                                        wounds += gameState.getWounds(physicalCard);
                                    }
                                }
                                return wounds;

                            }
                        }, Keyword.EASTERLING));
        return action;
    }
}
