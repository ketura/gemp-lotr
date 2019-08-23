package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship or Regroup: Spot Elrond (or 2 [ELVEN] allies) to heal a companion or ally.
 */
public class Card3_022 extends AbstractEvent {
    public Card3_022() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Master of Healing", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return (Filters.canSpot(game, Filters.name("Elrond"))
                || PlayConditions.canSpot(game, 2, Culture.ELVEN, CardType.ALLY));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndHealCharactersEffect(action, playerId, Filters.or(CardType.COMPANION, CardType.ALLY)));
        return action;
    }
}
