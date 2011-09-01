package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
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
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), Filters.keyword(Keyword.TALE))) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.MANEUVER, "Discard your tale from play or from hand to heal your companion.");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose your tale", Filters.owner(playerId), Filters.keyword(Keyword.TALE)) {
                        @Override
                        protected void cardSelected(PhysicalCard tale) {
                            action.addCost(new DiscardCardFromPlayEffect(tale));
                        }
                    });
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose your companion", Filters.owner(playerId), Filters.type(CardType.COMPANION)) {
                        @Override
                        protected void cardSelected(PhysicalCard companion) {
                            action.addEffect(new HealCharacterEffect(companion));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
