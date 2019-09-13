package com.gempukku.lotro.cards.set30.smaug;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Smaug
 * Twilight Cost: 16
 * Type: Minion • Dragon
 * Strength: 17
 * Vitality: 5
 * Site: 7
 * Game Text: Damage +2. Fierce. Discard Smaug if not at a mountain. For each Dwarf, Man, and Elf you spot, Smaug's
 * twilight cost is -1. Regroup: Exert Smaug and discard another minion to discard a Free Peoples card (except a
 * companion, Bard, or a Ring).
 */
public class Card30_058 extends AbstractMinion {
    public Card30_058() {
        super(16, 17, 5, 7, Race.DRAGON, Culture.GUNDABAD, "Smaug", "The Golden", true);
        addKeyword(Keyword.DAMAGE, 2);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (!game.getModifiersQuerying().hasKeyword(game, game.getGameState().getCurrentSite(), Keyword.MOUNTAIN)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
	}
	
    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
		return - Filters.countActive(game, Filters.or(Race.DWARF, Race.MAN, Race.ELF));
    }
	
	@Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.MINION, Filters.not(self))
				&& PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));
			action.appendCost(
					new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION, Filters.not(self)));
			action.appendEffect(
					new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, Filters.and(Filters.not(CardType.COMPANION), Filters.not(PossessionClass.RING), Filters.not(Filters.name("Bard")))));
            return Collections.singletonList(action);
		}
		return null;
	}
}
