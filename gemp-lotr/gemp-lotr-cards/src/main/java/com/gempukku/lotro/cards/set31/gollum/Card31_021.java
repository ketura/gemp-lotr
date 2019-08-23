package com.gempukku.lotro.cards.set31.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: You may exert Gollum twice to play this event from your discard pile. Make a minion strength +2. If
 * that minion wins this skirmish, you may add a doubt.
 */
public class Card31_021 extends AbstractEvent {
    public Card31_021() {
        super(Side.SHADOW, 2, Culture.GOLLUM, "If He Loses", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION) {
            @Override
            protected void cardSelected(LotroGame game, final PhysicalCard card) {
                action.appendEffect(new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, card, 2)));
                action.appendEffect(
                        new AddUntilEndOfPhaseActionProxyEffect(new AbstractActionProxy() {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                        if (TriggerConditions.winsSkirmish(game, effectResult, card)
                                && playerId.equals(card.getOwner())) {
                            OptionalTriggerAction action = new OptionalTriggerAction(self);
                            action.setVirtualCardAction(true);
                            action.appendEffect(
                                    new AddBurdenEffect(self.getOwner(), self, 1));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                }));
            }
        });
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.canExert(self, game, 2, Filters.name("Gollum"))
				&& PlayConditions.canPlayFromDiscard(playerId, game, self)) {
            final PlayEventAction playCardAction = getPlayEventCardAction(playerId, game, self);
			playCardAction.appendCost(
					new ChooseAndExertCharactersEffect(playCardAction, playerId, 1, 1, 2, Filters.name("Gollum")));
            return Collections.singletonList(playCardAction);
        }
        return null;
    }
}
