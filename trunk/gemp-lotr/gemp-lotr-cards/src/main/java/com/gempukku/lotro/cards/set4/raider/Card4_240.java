package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 4
 * Type: Event
 * Game Text: Regroup: Exert a [RAIDER] Man to add a burden for each companion over 4. The Free Peoples player may
 * discard 2 companions (except the Ring-bearer) to prevent this.
 */
public class Card4_240 extends AbstractOldEvent {
    public Card4_240() {
        super(Side.SHADOW, Culture.RAIDER, "New Fear", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game, Filters.culture(Culture.RAIDER), Filters.race(Race.MAN));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.RAIDER), Filters.race(Race.MAN)));
        int burdens = Math.max(0, Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) - 4);
        action.appendEffect(
                new PreventableEffect(action,
                        new AddBurdenEffect(self, burdens),
                        game.getGameState().getCurrentPlayerId(),
                        new ChooseAndDiscardCardsFromPlayEffect(action, game.getGameState().getCurrentPlayerId(), 2, 2, Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER)))));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 4;
    }
}
