package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a [GONDOR] Man to wound a minion once (or twice if that minion is an Uruk-hai).
 */
public class Card4_115 extends AbstractEvent {
    public Card4_115() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Defend It and Hope", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.GONDOR, Race.MAN);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Race.MAN));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION, Filters.canTakeWounds(self, 1)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        boolean urukHai = (card.getBlueprint().getRace() == Race.URUK_HAI);
                        action.insertEffect(
                                new WoundCharactersEffect(self, card));
                        if (urukHai)
                            action.insertEffect(
                                    new WoundCharactersEffect(self, card));
                    }
                });
        return action;
    }
}
