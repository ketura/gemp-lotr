package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChoiceCost;
import com.gempukku.lotro.cards.costs.ChooseAndDiscardCardsFromPlayCost;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.ChooseableCost;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 4
 * Game Text: Underground. Maneuver: Discard your tale from play or from hand to heal your companion.
 */
public class Card1_343 extends AbstractSite {
    public Card1_343() {
        super("Balin's Tomb", 4, 3, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), Filters.keyword(Keyword.TALE))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);
            List<ChooseableCost> possibleCosts = new LinkedList<ChooseableCost>();
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromPlayCost(action, playerId, 1, 1, Filters.owner(playerId), Filters.keyword(Keyword.TALE)));
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, 1, Filters.keyword(Keyword.TALE)) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard Tale from hands";
                        }
                    });
            action.appendCost(
                    new ChoiceCost(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndHealCharacterEffect(action, playerId, "Choose your companion", Filters.owner(playerId), Filters.type(CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
