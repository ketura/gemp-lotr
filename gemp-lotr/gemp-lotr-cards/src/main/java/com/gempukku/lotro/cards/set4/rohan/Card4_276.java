package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a [ROHAN] Man wins a skirmish, you may place a [ROHAN] token
 * on this card. Regroup: Discard a Shadow condition for each [ROHAN] token here. Discard this condition.
 */
public class Card4_276 extends AbstractPermanent {
    public Card4_276() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Fortress Never Fallen", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.and(Culture.ROHAN, Race.MAN))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ROHAN));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)) {
            int tokenCount = game.getGameState().getTokenCount(self, Token.ROHAN);
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, tokenCount, tokenCount, Side.SHADOW, CardType.CONDITION));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
