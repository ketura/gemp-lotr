package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time Gandalf wins a skirmish, you may place a [GANDALF] token here.
 * Skirmish: Wound a minion skirmishing Gandalf for each [GANDALF] token here. Discard this condition.
 */
public class Card4_088 extends AbstractPermanent {
    public Card4_088() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Behold the White Rider", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game, effectResult, Filters.gandalf)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.GANDALF));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            int tokenCount = game.getGameState().getTokenCount(self, Token.GANDALF);
            for (int i = 0; i < tokenCount; i++)
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.inSkirmishAgainst(Filters.gandalf)));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
