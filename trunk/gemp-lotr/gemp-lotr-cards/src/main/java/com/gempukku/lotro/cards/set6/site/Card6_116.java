package com.gempukku.lotro.cards.set6.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
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
 * Type: Site
 * Site: 1T
 * Game Text: Fellowship: Exert Aragorn twice to play a companion with the Aragorn signet from your draw deck.
 */
public class Card6_116 extends AbstractSite {
    public Card6_116() {
        super("Westfold", Block.TWO_TOWERS, 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, 2, Filters.name("Aragorn"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Aragorn")));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, CardType.COMPANION, Signet.ARAGORN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
