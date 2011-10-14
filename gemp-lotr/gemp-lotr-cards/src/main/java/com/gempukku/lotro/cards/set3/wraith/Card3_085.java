package com.gempukku.lotro.cards.set3.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot a Nazgul to wound Gandalf twice. The Free Peoples player may discard 2 [GANDALF] cards
 * from hand to prevent this.
 */
public class Card3_085 extends AbstractEvent {
    public Card3_085() {
        super(Side.SHADOW, Culture.WRAITH, "Too Great and Terrible", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new UnrespondableEffect() {
                            @Override
                            public String getText(LotroGame game) {
                                return "Wound Gandalf twice";
                            }

                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.name("Gandalf")));
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.name("Gandalf")));
                            }
                        }, Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                        new ChooseAndDiscardCardsFromHandEffect(action, game.getGameState().getCurrentPlayerId(), 2, 2, Filters.culture(Culture.GANDALF))));
        return action;
    }
}
