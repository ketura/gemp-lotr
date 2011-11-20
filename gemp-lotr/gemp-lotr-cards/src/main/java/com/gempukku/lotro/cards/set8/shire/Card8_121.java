package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

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
 * Game Text: To play, spot a [ROHAN] companion. Skirmish: Discard 4 cards from hand to make Merry strength +2 for each
 * [ROHAN] companion you spot.
 */
public class Card8_121 extends AbstractCompanion {
    public Card8_121() {
        super(2, 5, 4, 6, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Merry", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.ROHAN, CardType.COMPANION);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 4, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 4));
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, Culture.ROHAN, CardType.COMPANION) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, self, spotCount), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
