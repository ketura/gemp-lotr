package com.gempukku.lotro.cards.set9.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: To play, spot an Elf. Bearer must be an Elf or Hobbit. Maneuver: Discard this possession to make a Shadow
 * player return a minion with strength 6 or less to its owner's hand.
 */
public class Card9_013 extends AbstractAttachableFPPossession {
    public Card9_013() {
        super(1, 0, 0, Culture.ELVEN, null, "Elven Rope", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Race.ELF, Race.HOBBIT);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new ChooseAndReturnCardsToHandEffect(action, opponentId, 1, 1, CardType.MINION, Filters.lessStrengthThan(7)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
