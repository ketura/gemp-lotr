package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.DiscardBottomCardFromDeckEffect;
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
 * Set: Mount Doom
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Skirmish: Discard a card from hand to discard the bottom card of your draw deck. If the bottom card was
 * an [ELVEN] card, each minion skirmishing Arwen is strength -3.
 */
public class Card10_005 extends AbstractCompanion {
    public Card10_005() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, Signet.ARAGORN, "Arwen", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1));
            action.appendEffect(
                    new DiscardBottomCardFromDeckEffect(playerId) {
                        @Override
                        protected void discardedCardCallback(PhysicalCard card) {
                            if (card.getBlueprint().getCulture() == Culture.ELVEN) {
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(self)), -3), Phase.SKIRMISH));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
