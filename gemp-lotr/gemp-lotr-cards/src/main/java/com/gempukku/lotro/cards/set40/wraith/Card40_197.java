package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.PutOnTheOneRingEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;

/**
 * Title: The Ring's Compulsion
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 1
 * Type: Event - Maneuver
 * Card Number: 1U197
 * Game Text: Exert X Nazgul to make the Ring-bearer wear The One Ring until the regroup phase.
 * The Free Peoples player may exert the Ring-bearer X times to prevent this.
 */
public class Card40_197 extends AbstractEvent {
    public Card40_197() {
        super(Side.SHADOW, 1, Culture.WRAITH, "The Ring's Compulsion", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, Integer.MAX_VALUE, Race.NAZGUL) {
                    @Override
                    protected void cardsToBeExertedCallback(Collection<PhysicalCard> characters) {
                        final int exertCount = characters.size();
                        action.appendEffect(
                                new PreventableEffect(action,
                                        new PutOnTheOneRingEffect(), GameUtils.getFreePeoplePlayer(game),
                                        new PreventableEffect.PreventionCost() {
                                            @Override
                                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                return new ChooseAndExertCharactersEffect(subAction, playerId, 1, 1, exertCount, Filters.ringBearer);
                                            }
                                        }));
                    }
                });
        return action;
    }
}
