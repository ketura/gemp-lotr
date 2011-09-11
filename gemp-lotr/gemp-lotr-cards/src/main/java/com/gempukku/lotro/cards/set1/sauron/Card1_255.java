package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Exert a [SAURON] Orc to wound a character he is skirmishing.
 */
public class Card1_255 extends AbstractEvent {
    public Card1_255() {
        super(Side.SHADOW, Culture.SAURON, "Mordor's Strength", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose a SAURON Orc", true, Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard minion) {
                        super.cardSelected(minion);
                        Skirmish skirmish = game.getGameState().getSkirmish();
                        if (skirmish != null
                                && skirmish.getShadowCharacters().contains(minion)
                                && skirmish.getFellowshipCharacter() != null) {
                            action.addEffect(
                                    new WoundCharacterEffect(skirmish.getFellowshipCharacter()));
                        }
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
