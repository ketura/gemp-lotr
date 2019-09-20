package com.gempukku.lotro.cards.set32.smaug;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.PutPlayedEventIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Smaug
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Play Smaug from your draw deck or discard pile at twilight cost -8. He is strength -4 and
 * damage -1 until the regroup phase. You may discard 2 Orcs to take this card back into hand.
 */
public class Card32_060 extends AbstractEvent {
    public Card32_060() {
        super(Side.SHADOW, 0, Culture.GUNDABAD, "Smaug's Awakening", Phase.SHADOW);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new ChooseAndPlayCardFromDeckEffect(playerId, -8, Filters.name("Smaug")) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play Smaug from your deck";
                    }
                });
        possibleCosts.add(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, -8, Filters.name("Smaug")) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play Smaug from your discard pile";
                    }
                });
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new StrengthModifier(self, Filters.name("Smaug"), -4), Phase.REGROUP));
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new KeywordModifier(self, Filters.name("Smaug"), Keyword.DAMAGE, -1), Phase.REGROUP));
        if (PlayConditions.canDiscardFromPlay(self, game, 2, Race.ORC)) {
            action.appendEffect(
                    new PlayoutDecisionEffect(playerId,
                            new YesNoDecision("Do you want to return " + GameUtils.getCardLink(self) + " back into hand?") {
                                @Override
                                public void yes() {
                                    SubAction subAction = new SubAction(action);
                                    subAction.appendCost(
                                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Race.ORC));
                                    subAction.appendEffect(
                                            new PutPlayedEventIntoHandEffect(action));
                                    game.getActionsEnvironment().addActionToStack(subAction);
                                }
                            }));
        }
        return action;
    }
}
