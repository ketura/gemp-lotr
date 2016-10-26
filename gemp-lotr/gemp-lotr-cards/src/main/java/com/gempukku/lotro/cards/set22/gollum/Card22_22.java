package com.gempukku.lotro.cards.set22.gollum;

OptionalTriggerAction
import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Each time the fellowship moves, add (2). Response: If Bilbo exerts or takes a wound, exert
 * a minion and discard a card from hand to discard a [SHIRE] card (except Bilbo). The Free Peoples player
 * may discard 2 cards from hand to prevent this.
 */
public class Card22_22 extends AbstractPermanent {
    public Card22_22() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Riddles in the Dark", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
	
    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
		if (TriggerConditions.forEachExerted(game, effectResult, Filters.name("Bilbo")) || TriggerConditions.forEachWounded(game, effectResult, Filters.name("Bilbo"))) {
			OptionalTriggerAction action = new OptionalTriggerAction(self);
			action.appendCost(
					new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
			action.appendCost(
					new ChooseAndDiscardCardsFromHandEffect(subAction, playerId, true, 1, 1));
            action.appendEffect(
					new PreventableEffect(action,
							new UnrespondableEffect() {
				@Override
				public String getText(LotroGame game) {
					return "Choose a SHIRE card";
				}
				
				@Override
				protected void doPlayEffect(LotroGame game) {
					action.appendEffect(
							new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.SHIRE));
				}
            }, Collections.singletonList(game.getGameState().getCurrentPlayerId()),
					new PreventableEffect.PreventionCost() {
				@Override
				public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
					return new ChooseAndDiscardCardsFromHandEffect(subAction, playerId, true, 2, 2);
				}
            }
			));
        }
        return action;
	}
}