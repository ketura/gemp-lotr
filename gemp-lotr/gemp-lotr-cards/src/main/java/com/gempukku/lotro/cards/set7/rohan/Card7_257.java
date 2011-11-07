package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Skirmish: Exert this companion to make him strength +1 for each mounted companion you spot.
 */
public class Card7_257 extends AbstractCompanion {
    public Card7_257() {
        super(2, 6, 3, Culture.ROHAN, Race.MAN, null, "Veteran Horseman");
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, CardType.COMPANION, Filters.mounted) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, self, spotCount), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
