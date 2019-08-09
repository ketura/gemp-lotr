package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddTokenEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.modifiers.HasInitiativeModifier;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Possession • Support Area
 * Game Text: When you play this possession, you may add a [RAIDER] token here. While you can spot 6 [RAIDER] tokens
 * and a [RAIDER] Man, the Shadow has initiative, regardless of the Free Peoples player’s hand. Regroup: Add (1) for
 * each [RAIDER] token you can spot. Discard this possession.
 */
public class Card8_059 extends AbstractPermanent {
    public Card8_059() {
        super(Side.SHADOW, 1, CardType.POSSESSION, Culture.RAIDER, Zone.SUPPORT, "Corsair War Galley");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new HasInitiativeModifier(self,
                        new AndCondition(
                                new SpotCondition(Culture.RAIDER, Race.MAN),
                                new Condition() {
                                    @Override
                                    public boolean isFullfilled(LotroGame game) {
                                        return GameUtils.getSpottableTokensTotal(game, Token.RAIDER) >= 6;
                                    }
                                }
                        ), Side.SHADOW));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.RAIDER));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);

            action.appendEffect(
                    new AddTwilightEffect(self, GameUtils.getSpottableTokensTotal(game, Token.RAIDER)));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
