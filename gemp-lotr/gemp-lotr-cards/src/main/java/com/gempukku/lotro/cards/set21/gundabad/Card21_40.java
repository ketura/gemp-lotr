package com.gempukku.lotro.cards.set21.gundabad;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 3
 * Site: 3
 * Game Text: When you play this minion, you may play the fellowship's next site (replacing an opponent's site
 * if necessary). Shadow: Exert this minion twice to make each site on the adventure path gain battleground,
 * mountain, forest, or underground until the end of the turn.
 */
public class Card21_40 extends AbstractMinion {
    public Card21_40() {
        super(4, 8, 3, 3, Race.ORC, Culture.GUNDABAD, "Watchful Orc");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber() + 1));
            return Collections.singletonList(action);
        }
        return null;
    }
	
	@Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game, 2, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
					new SelfExertEffect(action, self));
			action.appendEffect(
					new PlayoutDecisionEffect(self.getOwner(),
							new MultipleChoiceAwaitingDecision(1, "Choose type", new String[]{"battleground", "mountain", "forest", "underground"}) {
				@Override
				protected void validDecisionMade(int index, String result) {
					Keyword keyword;
					if (index == 0)
						keyword = Keyword.BATTLEGROUND;
					else if (index == 1)
						keyword = Keyword.MOUNTAIN;
					else if (index == 2)
						keyword = Keyword.FOREST;
					else
						keyword = Keyword.UNDERGROUND;
					action.appendEffect(
							new AddUntilEndOfTurnModifierEffect(
									new KeywordModifier(self, Filters.and(CardType.SITE, Zone.ADVENTURE_PATH), keyword)));
				}
			}));
			return Collections.singletonList(action);
		}
		return null;
	}
}