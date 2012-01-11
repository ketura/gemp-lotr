package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 5
 * Game Text: Each time the fellowship moves, you may add (1) and remove a Free Peoples culture token to reinforce
 * a [ROHAN] token.
 */
public class Card13_132 extends AbstractCompanion {
    public Card13_132() {
        super(2, 6, 3, 5, Culture.ROHAN, Race.MAN, null, "Merchant of Westfold");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
                && PlayConditions.canRemoveAnyCultureTokens(game, 1, Side.FREE_PEOPLE)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 1));
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, null, 1, Side.FREE_PEOPLE));
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.ROHAN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
