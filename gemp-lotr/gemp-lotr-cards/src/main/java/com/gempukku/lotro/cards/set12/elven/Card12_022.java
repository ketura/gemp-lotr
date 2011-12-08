package com.gempukku.lotro.cards.set12.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Maneuver: Reveal the top card of your draw deck. If it is an [ELVEN] card, Rumil is an archer until the
 * regroup phase.
 */
public class Card12_022 extends AbstractCompanion {
    public Card12_022() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, null, "Rumil", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                if (card.getBlueprint().getCulture() == Culture.ELVEN)
                                    action.appendEffect(
                                            new AddUntilStartOfPhaseModifierEffect(
                                                    new KeywordModifier(self, self, Keyword.ARCHER), Phase.MANEUVER));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
