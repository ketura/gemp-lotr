package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Maneuver: Exert a Dwarf companion and discard the top 3 cards from your draw
 * deck to discard either a Shadow condition from a Dwarf or a weather condition.
 */
public class Card2_011 extends AbstractPermanent {
    public Card2_011() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.FREE_SUPPORT, "Make Light of Burdens");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF), Filters.type(CardType.COMPANION), Filters.canExert())
                && game.getGameState().getDeck(playerId).size() >= 3) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);
            action.appendCost(
                    new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.DWARF), Filters.type(CardType.COMPANION)));
            action.appendCost(
                    new DiscardTopCardFromDeckEffect(playerId));
            action.appendCost(
                    new DiscardTopCardFromDeckEffect(playerId));
            action.appendCost(
                    new DiscardTopCardFromDeckEffect(playerId));
            action.appendEffect(
                    new ChooseActiveCardEffect(playerId, "Choose condition",
                            Filters.or(
                                    Filters.and(Filters.side(Side.SHADOW), Filters.type(CardType.CONDITION), Filters.attachedTo(Filters.race(Race.DWARF))),
                                    Filters.and(Filters.keyword(Keyword.WEATHER)), Filters.type(CardType.CONDITION))) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.appendEffect(
                                    new DiscardCardsFromPlayEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
