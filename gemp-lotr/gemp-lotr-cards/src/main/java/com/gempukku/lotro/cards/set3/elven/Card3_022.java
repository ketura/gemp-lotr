package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship or Regroup: Spot Elrond (or 2 [ELVEN] allies) to heal a companion or ally.
 */
public class Card3_022 extends AbstractOldEvent {
    public Card3_022() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Master of Healing", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Elrond"))
                || Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ELVEN), Filters.type(CardType.ALLY)) >= 2);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndHealCharactersEffect(action, playerId, Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY))));
        return action;
    }
}
