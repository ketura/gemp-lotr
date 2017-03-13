package com.gempukku.lotro.cards.set31.spider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Spider
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Each time a companion or a follower is played at site 5, the Free Peoples player must exert
 * 2 [DWARVEN] companions. Shadow: Discard this condition or exert 3 Spiders to draw 3 cards.
 */
public class Card31_058 extends AbstractPermanent {
    public Card31_058() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SPIDER, Zone.SUPPORT, "Enchanted River", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.or(CardType.COMPANION, CardType.FOLLOWER))
                && game.getGameState().getCurrentSiteNumber() == 5 && game.getGameState().getCurrentSiteBlock() == Block.HOBBIT) {
            PlayCardResult playCardResult = (PlayCardResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
				new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 2, 2, CardType.COMPANION, Culture.DWARVEN));
            return Collections.singletonList(action);
        }
        return null;
    }
	
	
    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new SelfDiscardEffect(self) {
                @Override
                public String getText(LotroGame game) {
                    return "Discard this condition";
                }
            });
			possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 3, 3, Race.SPIDER) {
                @Override
                public String getText(LotroGame game) {
                    return "Exert 3 Spiders";
                }
            });
            action.appendCost(
					new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 3));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
	}
}