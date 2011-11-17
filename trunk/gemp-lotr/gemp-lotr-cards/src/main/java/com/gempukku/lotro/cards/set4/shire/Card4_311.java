package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveCharacterFromSkirmishEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Skirmish: If Merry is not assigned to a skirmish, discard him to remove a [SAURON] Orc from a skirmish
 * involving an unbound companion.
 */
public class Card4_311 extends AbstractCompanion {
    public Card4_311() {
        super(1, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.GANDALF, "Merry", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.notAssignedToSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self)
                && PlayConditions.canDiscardFromPlay(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose SAURON Orc", Culture.SAURON, Race.ORC, Filters.inSkirmishAgainst(Filters.unboundCompanion)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new RemoveCharacterFromSkirmishEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
