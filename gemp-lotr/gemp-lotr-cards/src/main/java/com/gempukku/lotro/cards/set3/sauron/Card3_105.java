package com.gempukku.lotro.cards.set3.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collections;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot a [SAURON] minion to discard Bilbo. The Free Peoples player may discard 2 Free Peoples
 * conditions to prevent this.
 */
public class Card3_105 extends AbstractEvent {
    public Card3_105() {
        super(Side.SHADOW, Culture.SAURON, "Why Shouldn't I Keep It?", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.type(CardType.MINION));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.name("Bilbo")) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Discard Bilbo";
                            }
                        }, Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                        new ChooseAndDiscardCardsFromPlayEffect(action, game.getGameState().getCurrentPlayerId(), 2, 2, Filters.side(Side.FREE_PEOPLE), Filters.type(CardType.CONDITION))));
        return action;
    }
}
