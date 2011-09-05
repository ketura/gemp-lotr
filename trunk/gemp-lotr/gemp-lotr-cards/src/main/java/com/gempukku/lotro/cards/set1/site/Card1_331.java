package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 2
 * Type: Site
 * Site: 2
 * Game Text: Plains. Skirmish: Exert your companion or minion to make that character strength +2.
 */
public class Card1_331 extends AbstractSite {
    public Card1_331() {
        super("Ettenmoors", 2, 2, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.MINION)), Filters.owner(playerId), Filters.canExert())) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.SKIRMISH, "Exert your companion or minion to make that character strength +2.");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose your companion or minion", Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.MINION)), Filters.owner(playerId), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.addCost(new ExertCharacterEffect(card));
                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), 2), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
