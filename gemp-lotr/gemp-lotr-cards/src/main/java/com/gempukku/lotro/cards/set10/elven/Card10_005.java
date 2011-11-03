package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifier;
import com.gempukku.lotro.cards.effects.DiscardBottomCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.NegativeEvaluator;
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
 * Type: Companion Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Skirmish: Discard a card from hand to discard the bottom card of your draw deck. If the bottom card was an [ELVEN] card, each minion skirmishing Arwen is strength -3. 
 * Rarity: U
 */
public class Card10_005 extends AbstractCompanion {
    public Card10_005() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, Signet.ARAGORN, "Arwen", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromHand(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.setText("Discard the bottom card of your draw deck. If the bottom card was an Elven card, each minion skirmishing Arwen is strength -3.");
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1));
            action.appendEffect(
            		new DiscardBottomCardFromDeckEffect(playerId) {
            			@Override
                        protected void discardedCardCallback(PhysicalCard card) {
                            if (card.getCulture() == Culture.ELVEN) {
                            	action.appendEffect(
                            			new AddUntilEndOfPhaseModifier(new StrengthModifier(self, Fitlers.in(CardType.MINION, Filters.inSkirmishAgainst(self)), -3), Phase.SKIRMISH);
                            }
            			}
            		}
            return Collections.singletonList(action);
        }
        return null;
    }
}
