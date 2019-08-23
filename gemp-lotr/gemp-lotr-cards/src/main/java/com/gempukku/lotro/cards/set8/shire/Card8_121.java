package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(2, 5, 4, 6, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Merry", "Noble Warrior", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ROHAN, CardType.COMPANION);
    }

    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
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
                                            new StrengthModifier(self, self, 2 * spotCount)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
