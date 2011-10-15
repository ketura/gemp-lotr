package com.gempukku.lotro.cards.set3.sauron;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot a [SAURON] minion to wound Galadriel 3 times. The Free Peoples player may discard 2 Elves
 * to prevent this.
 */
public class Card3_103 extends AbstractOldEvent {
    public Card3_103() {
        super(Side.SHADOW, Culture.SAURON, "Terrible as the Dawn", Phase.MANEUVER);
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
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new UnrespondableEffect() {
                            @Override
                            public String getText(LotroGame game) {
                                return "Wound Galadriel 3 times";
                            }

                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.name("Galadriel")));
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.name("Galadriel")));
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.name("Galadriel")));
                            }
                        }, Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                        new ChooseAndDiscardCardsFromPlayEffect(action, game.getGameState().getCurrentPlayerId(), 2, 2, Filters.race(Race.ELF)) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Discard 2 Elves from play";
                            }
                        }
                ));
        return action;
    }
}
