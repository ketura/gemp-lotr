package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;

/**
 * 1
 * The Ring's Compulsion
 * Ringwraith	Event • Maneuver
 * Exert X Nazgul to make the Ring-bearer wear The One Ring until the regroup phase.
 * The Free Peoples player may exert the Ring-bearer X times to prevent this.
 */
public class Card20_300 extends AbstractEvent {
    public Card20_300() {
        super(Side.SHADOW, 1, Culture.WRAITH, "The Ring's Compulsion", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 0, Integer.MAX_VALUE, Race.NAZGUL) {
                    @Override
                    protected void cardsToBeExertedCallback(Collection<PhysicalCard> characters) {
                        final int count = characters.size();
                        action.appendEffect(
                                new PreventableEffect(action, new PutOnTheOneRingEffect(), game.getGameState().getCurrentPlayerId(),
                                        new PreventableEffect.PreventionCost() {
                                            @Override
                                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                return new ChooseAndExertCharactersEffect(action, playerId, 1, 1, count, Filters.ringBearer) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Exert Ring-bearer "+count+" times";
                                                    }
                                                };
                                            }
                                        }));
                    }
                });
        return action;
    }
}
