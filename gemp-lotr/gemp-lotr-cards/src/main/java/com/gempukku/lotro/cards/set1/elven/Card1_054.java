package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.PutCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseAnyCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Fellowship: Reveal an [ELVEN] card from hand and place it beneath your draw
 * deck.
 */
public class Card1_054 extends AbstractLotroCardBlueprint {
    public Card1_054() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.ELVEN, "Mallorn-trees", "1_54");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {

        if (PlayConditions.canPlayDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.FREE_SUPPORT);
            return Collections.singletonList(action);
        }

        if (self.getZone() == Zone.FREE_SUPPORT
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ELVEN)).size() > 0) {
            final CostToEffectAction action = new CostToEffectAction(self, "Reveal an ELVEN card from hand and place it beneath your draw deck");
            action.addCost(
                    new ChooseAnyCardEffect(playerId, "Choose ELVEN card", Filters.zone(Zone.HAND), Filters.owner(playerId), Filters.culture(Culture.ELVEN)) {
                        @Override
                        protected void cardSelected(PhysicalCard elvenCard) {
                            action.addEffect(new PutCardFromHandOnBottomOfDeckEffect(elvenCard));
                        }
                    });
            return Collections.singletonList(action);
        }

        return null;
    }
}
