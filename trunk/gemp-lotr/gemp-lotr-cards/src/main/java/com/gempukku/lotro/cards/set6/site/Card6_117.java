package com.gempukku.lotro.cards.set6.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeadPileEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Twilight Cost: 0
 * Type: Site
 * Site: 3T
 * Game Text: Sanctuary. Fellowship: Exert 3 companions with the Gandalf signet to play an unbound companion from
 * your dead pile.
 */
public class Card6_117 extends AbstractSite {
    public Card6_117() {
        super("Mudeseld", Block.TWO_TOWERS, 3, 0, Direction.RIGHT);

    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExertMultiple(self, game, 1, 3, CardType.COMPANION, Signet.GANDALF)
                && PlayConditions.canPlayFromDeadPile(playerId, game, Filters.unboundCompanion)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 3, 3, CardType.COMPANION, Signet.GANDALF));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeadPileEffect(playerId, game.getGameState().getDeadPile(playerId), Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
