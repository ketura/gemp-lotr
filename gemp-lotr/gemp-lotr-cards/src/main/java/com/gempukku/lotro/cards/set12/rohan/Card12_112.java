package com.gempukku.lotro.cards.set12.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 7
 * Game Text: While you can spot a [ROHAN] Man, Eomer's twilight cost is -1. Maneuver: If you can spot more minions than
 * companions, exert Eomer to play a [ROHAN] companion from your draw deck.
 */
public class Card12_112 extends AbstractCompanion {
    public Card12_112() {
        super(3, 7, 3, 7, Culture.ROHAN, Race.MAN, null, "Eomer", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, Culture.ROHAN, Race.MAN))
            return -1;
        return 0;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.MINION) > Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Culture.ROHAN, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
