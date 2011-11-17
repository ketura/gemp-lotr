package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Each time a roaming [SAURON] minion takes a wound, you may add a [SAURON] token here. Regroup: Exert
 * an Uruk-hai or discard a [SAURON] minion to add a [SAURON] token here. Skirmish: Remove 2 [SAURON] tokens from here
 * to make a [SAURON] minion strength +2.
 */
public class Card10_096 extends AbstractPermanent {
    public Card10_096() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Rank and File", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.isWounded(game, effectResult, Culture.SAURON, CardType.MINION, Keyword.ROAMING)) {
            WoundResult woundResult = (WoundResult) effectResult;
            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
            for (PhysicalCard minion : Filters.filter(woundResult.getWoundedCards(), game.getGameState(), game.getModifiersQuerying(), Culture.SAURON, CardType.MINION, Keyword.ROAMING)) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new AddTokenEffect(self, self, Token.SAURON));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && (PlayConditions.canExert(self, game, Race.URUK_HAI) || PlayConditions.canDiscardFromPlay(self, game, Culture.SAURON, CardType.MINION))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert an Uruk-hai";
                        }
                    });
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.SAURON, CardType.MINION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard a SAURON minion";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SAURON));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveTokens(game, self, Token.SAURON, 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.SAURON, 2));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.SAURON, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
