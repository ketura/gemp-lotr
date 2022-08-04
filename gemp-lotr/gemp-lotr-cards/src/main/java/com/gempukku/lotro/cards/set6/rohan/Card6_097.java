package com.gempukku.lotro.cards.set6.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTokenEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.RemoveTokenEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. When you play this condition, you may spot a [ROHAN] Man to place 2 [ROHAN]
 * tokens here. Skirmish: Discard this condition or remove a [ROHAN] token from here to make a [ROHAN] Man skirmishing
 * an exhausted minion strength +2.
 */
public class Card6_097 extends AbstractPermanent {
    public Card6_097() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ROHAN, "We Left None Alive", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ROHAN, 2));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<>();
            possibleCosts.add(
                    new RemoveTokenEffect(self, self, Token.ROHAN) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove a ROHAN token from here";
                        }
                    });
            possibleCosts.add(
                    new SelfDiscardEffect(self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this condition";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.ROHAN, Race.MAN, Filters.inSkirmishAgainst(CardType.MINION, Filters.exhausted)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
