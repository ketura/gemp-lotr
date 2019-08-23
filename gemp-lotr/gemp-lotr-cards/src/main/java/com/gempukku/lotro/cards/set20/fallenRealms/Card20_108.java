package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
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
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                int wounds = 0;
                                if (Filters.inSkirmish.accepts(game, cardAffected)) {
                                    for (PhysicalCard physicalCard : Filters.filterActive(game, Filters.character, Filters.inSkirmish, Filters.wounded)) {
                                        wounds += game.getGameState().getWounds(physicalCard);
                                    }
                                }
                                return wounds;

                            }
                        }, Keyword.EASTERLING));
        return action;
    }
}
