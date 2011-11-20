package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 5
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: To play, spot a [GONDOR] Man. Skirmish: Discard 4 cards from hand to wound a minion Pippin is skirmishing
 * twice if that minion bears a fortification.
 */
public class Card8_122 extends AbstractCompanion {
    public Card8_122() {
        super(2, 5, 4, 6, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Pippin", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 4, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 4));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.inSkirmishAgainst(self)) {
                        @Override
                        protected void woundedCardsCallback(Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                if (Filters.hasAttached(Keyword.FORTIFICATION).accepts(game.getGameState(), game.getModifiersQuerying(), card))
                                    action.appendEffect(
                                            new WoundCharactersEffect(self, card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
