package com.gempukku.lotro.cards.set32.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 5
 * Game Text: Fierce. The Witch-king is twilight cost -1 for each Wise character you spot.
 * Skirmish: Discard 3 cards from hand to make a [WRAITH] minion damage +1.
 */
public class Card32_075 extends AbstractMinion {
    public Card32_075() {
        super(8, 14, 4, 5, Race.NAZGUL, Culture.WRAITH, Names.witchKing, "Revived", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        return -Filters.countActive(game, Keyword.WISE);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && game.getGameState().getHand(playerId).size() >= 3) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a WRAITH minion", Culture.WRAITH, CardType.MINION) {
                        @Override
                        public void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(card), Keyword.DAMAGE, 1)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
