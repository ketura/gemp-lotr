package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Companion • Elf
 * Strength: 3
 * Vitality: 3
 * Resistance: 6
 * Game Text: When Galadriel is in your starting fellowship, her twilight cost is -3. Fellowship or Regroup: Discard
 * an [ELVEN] event from hand to discard a Shadow condition or Shadow possession.
 */
public class Card10_011 extends AbstractCompanion {
    public Card10_011() {
        super(3, 3, 3, 6, Culture.ELVEN, Race.ELF, null, "Galadriel", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP)
            return -3;
        return 0;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self) || PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self))
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.ELVEN, CardType.EVENT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.ELVEN, CardType.EVENT));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, Filters.or(CardType.CONDITION, CardType.POSSESSION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
