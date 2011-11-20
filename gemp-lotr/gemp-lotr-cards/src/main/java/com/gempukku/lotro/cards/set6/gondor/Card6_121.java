package com.gempukku.lotro.cards.set6.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Ring-bound. Ranger. If Faramir is in your starting fellowship, his twilight cost is -1.
 * Fellowship: Play a ranger to heal a Ring-bound companion.
 */
public class Card6_121 extends AbstractCompanion {
    public Card6_121() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, Signet.GANDALF, "Faramir", true);
        addKeyword(Keyword.RING_BOUND);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP)
            return -1;
        return 0;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromHand(playerId, game, Keyword.RANGER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Keyword.RANGER));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION, Keyword.RING_BOUND));
            return Collections.singletonList(action);
        }
        return null;
    }
}
