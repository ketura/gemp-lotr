package com.gempukku.lotro.cards.set32.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 4
 * Game Text: Damage +1. Maneuver: Exert Azog at a battleground to play a Moria minion from your
 * discard pile. Its twilight cost is -2.
 */
public class Card32_028 extends AbstractMinion {
    public Card32_028() {
        super(4, 9, 3, 4, Race.ORC, Culture.MORIA, "Azog", "Commander of the Wolves Army", true);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, -2, Culture.MORIA, CardType.MINION)
                && game.getModifiersQuerying().hasKeyword(game, game.getGameState().getCurrentSite(), Keyword.BATTLEGROUND)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, Culture.MORIA, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
