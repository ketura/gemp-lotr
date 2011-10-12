package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
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
        super("Pillars of the Kings", Block.FELLOWSHIP, 8, 6, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR)).size() > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, 1, 1, Filters.culture(Culture.GONDOR)));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.type(CardType.COMPANION), Filters.culture(Culture.GONDOR)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
