package com.gempukku.lotro.cards.set32.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTokenEffect;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Response: Each time a [MORIA] minion wins a skirmish, you may place a [MORIA] token here.
 * Shadow: If the Fellowship is at a battleground, discard this condition to add (2) for each [MORIA]
 * token here (limit (8)).
 */
public class Card32_033 extends AbstractPermanent {
    public Card32_033() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MORIA, "Were-worms", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.MORIA, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(new AddTokenEffect(self, self, Token.MORIA));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && game.getModifiersQuerying().hasKeyword(game, game.getGameState().getCurrentSite(), Keyword.BATTLEGROUND)) {
            ActivateCardAction action = new ActivateCardAction(self);
            int twilight = Math.min(8, 2 * game.getGameState().getTokenCount(self, Token.MORIA));
            action.appendCost(new SelfDiscardEffect(self));
            action.appendEffect(new AddTwilightEffect(self, twilight));
            return Collections.singletonList(action);
        }
        return null;
    }
}
