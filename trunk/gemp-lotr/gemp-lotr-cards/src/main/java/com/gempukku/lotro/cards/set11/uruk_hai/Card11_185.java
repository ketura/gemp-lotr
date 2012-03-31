package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AbstractPreventableCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Condition • Support Area
 * Game Text: When you play this condition, spot an [URUK-HAI] minion to add 3 [URUK-HAI] tokens here. Response: If an
 * [URUK-HAI] minion is about to take a wound, discard this condition or remove an [URUK-HAI] token from here to prevent
 * that.
 */
public class Card11_185 extends AbstractPermanent {
    public Card11_185() {
        super(Side.SHADOW, 4, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Fortitude", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.URUK_HAI, 3));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.URUK_HAI, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCost = new LinkedList<Effect>();
            possibleCost.add(
                    new RemoveTokenEffect(self, self, Token.URUK_HAI) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Remove an URUK_HAI token from here";
                        }
                    });
            possibleCost.add(
                    new SelfDiscardEffect(self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard this condition";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCost));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (AbstractPreventableCardEffect) effect, playerId, "Choose a minion to prevent wound to", Culture.URUK_HAI, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
