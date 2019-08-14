package com.gempukku.lotro.cards.set10.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Skirmish: Exert Eowyn to choose an opponent. That opponent must wound a minion for each wound on each
 * minion skirmishing Eowyn.
 */
public class Card10_072 extends AbstractCompanion {
    public Card10_072() {
        super(2, 6, 3, 6, Culture.ROHAN, Race.MAN, Signet.GANDALF, Names.eowyn, "Lady of Ithilien", true);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            int wounds = 0;
                            for (PhysicalCard minion : Filters.filterActive(game, CardType.MINION, Filters.inSkirmishAgainst(self)))
                                wounds += game.getGameState().getWounds(minion);
                            for (int i = 0; i < wounds; i++)
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, opponentId, 1, 1, CardType.MINION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
