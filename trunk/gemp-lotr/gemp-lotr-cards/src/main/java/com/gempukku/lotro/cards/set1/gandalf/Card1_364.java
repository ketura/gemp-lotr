package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Fellowship: Exert Gandalf to play a companion who has the Gandalf signet. The twilight cost of that
 * companion is -2.
 */
public class Card1_364 extends AbstractCompanion {
    public Card1_364() {
        super(4, 7, 4, Culture.GANDALF, Race.WIZARD, Signet.GANDALF, "Gandalf", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION), Filters.signet(Signet.GANDALF), Filters.playable(game, -2)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Filters.and(Filters.type(CardType.COMPANION), Filters.signet(Signet.GANDALF)), -2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
