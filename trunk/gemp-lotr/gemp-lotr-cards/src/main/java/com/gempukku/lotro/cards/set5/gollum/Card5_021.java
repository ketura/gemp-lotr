package com.gempukku.lotro.cards.set5.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Discard Smeagol to discard a minion. An opponent may exert a minion twice to prevent this.
 */
public class Card5_021 extends AbstractEvent {
    public Card5_021() {
        super(Side.FREE_PEOPLE, 1, Culture.GOLLUM, "Be Back Soon", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.name("Smeagol"));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.name("Smeagol")));
        action.appendEffect(
                new PreventableEffect(action,
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Discard a minion";
                            }
                        }, GameUtils.getOpponents(game, playerId),
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION)));
        return action;
    }
}
