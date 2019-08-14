package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a Hobbit bearing a weapon to wound a minion. That minion's owner may remove (3)
 * to prevent this.
 */
public class Card2_103 extends AbstractEvent {
    public Card2_103() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Hobbit Sword-play", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.HOBBIT, Filters.hasAttached(Filters.weapon));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, Filters.hasAttached(Filters.weapon)));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new PreventableEffect(action,
                                        new WoundCharactersEffect(self, card),
                                        Collections.singletonList(card.getOwner()),
                                        new PreventableEffect.PreventionCost() {
                                            @Override
                                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                return new RemoveTwilightEffect(3);
                                            }
                                        }
                                ));
                    }
                });
        return action;
    }
}
