package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 9
 * Type: Site
 * Site: 9
 * Game Text: Maneuver: Exert your minion to make that minion fierce until the regroup phase.
 */
public class Card1_360 extends AbstractSite {
    public Card1_360() {
        super("Emyn Muil", 9, 9, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), Filters.type(CardType.MINION), Filters.canExert())) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER, "Exert your minion to make that minion fierce until the regroup phase.");
            action.appendCost(
                    new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.owner(playerId), Filters.type(CardType.MINION), Filters.canExert()) {
                        @Override
                        protected void cardsSelected(Collection<PhysicalCard> minion, boolean success) {
                            super.cardsSelected(minion, success);
                            if (success) {
                                action.appendEffect(new CardAffectsCardEffect(self, minion));
                                action.appendEffect(
                                        new AddUntilStartOfPhaseModifierEffect(
                                                new KeywordModifier(self, Filters.in(minion), Keyword.FIERCE), Phase.REGROUP));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
