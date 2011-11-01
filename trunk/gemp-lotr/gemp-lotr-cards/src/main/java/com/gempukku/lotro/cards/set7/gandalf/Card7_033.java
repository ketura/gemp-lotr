package com.gempukku.lotro.cards.set7.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 5
 * Type: Event • Fellowship
 * Game Text: Exert Gandalf twice to remove all threats and to heal all companions of one culture (except [GANDALF]).
 */
public class Card7_033 extends AbstractEvent {
    public Card7_033() {
        super(Side.FREE_PEOPLE, 5, Culture.GANDALF, "Citadel to Gate", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game, 2, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.gandalf));
        action.appendEffect(
                new RemoveThreatsEffect(self, Integer.MAX_VALUE));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose companion of culture you wish to heal", CardType.COMPANION, Filters.wounded, Filters.not(Culture.GANDALF)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new HealCharactersEffect(self, card.getBlueprint().getCulture()));
                    }
                });
        return action;
    }
}
