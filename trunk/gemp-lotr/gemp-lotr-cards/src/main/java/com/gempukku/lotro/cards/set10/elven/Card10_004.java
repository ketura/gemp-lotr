package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Skirmish: Exert Aegnor to make a minion skirmishing an unbound [ELVEN] companion strength -1 for each
 * archer you spot.
 */
public class Card10_004 extends AbstractCompanion {
    public Card10_004() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, null, "Aegnor", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, Keyword.ARCHER) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.insertEffect(
                                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -spotCount, CardType.MINION, Filters.inSkirmishAgainst(Culture.ELVEN, Filters.unboundCompanion)));
                        }
                    });
            return Collections.singletonList(action);
        }

        return null;
    }
}
