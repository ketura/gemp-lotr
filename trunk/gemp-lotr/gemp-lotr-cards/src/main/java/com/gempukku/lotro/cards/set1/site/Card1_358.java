package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 8
 * Game Text: River. Fellowship: Discard a [GONDOR] card from hand to heal a [GONDOR] companion.
 */
public class Card1_358 extends AbstractSite {
    public Card1_358() {
        super("Pillars of the Kings", 8, 6, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR)).size() > 0) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Discard a GONDOR card from hand to heal a GONDOR companion.");
            action.addCost(
                    new ChooseCardsFromHandEffect(playerId, "Choose a GONDOR card", 1, 1, Filters.culture(Culture.GONDOR)) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            action.addCost(new DiscardCardFromHandEffect(selectedCards.get(0)));
                        }
                    });
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a GONDOR companion", Filters.type(CardType.COMPANION), Filters.culture(Culture.GONDOR)) {
                        @Override
                        protected void cardSelected(PhysicalCard gondorCompanion) {
                            action.addEffect(new HealCharacterEffect(playerId, gondorCompanion));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
