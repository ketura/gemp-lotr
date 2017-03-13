package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession • Support Area
 * Game Text: Fortification. To play, exert a [DWARVEN] companion. If the fellowship moves during the regroup phase,
 * discard this possession. At sites 7 and 8, each minion skirmishing a [DWARVEN] companion is strength -3.
 */
public class Card31_004 extends AbstractPermanent {
    public Card31_004() {
        super(Side.FREE_PEOPLE, 1, CardType.POSSESSION, Culture.DWARVEN, Zone.SUPPORT, "Great Barricade", null, true);
		addKeyword(Keyword.FORTIFICATION);
	}
	
    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.DWARF);
	}
	
	@Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayPermanentAction permanentAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        permanentAction.appendCost(
                new ChooseAndExertCharactersEffect(permanentAction, playerId, 1, 1, Race.DWARF));
        return permanentAction;
	}

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self,
                        Filters.and(
                                CardType.MINION,
                                Filters.inSkirmishAgainst(
                                        Filters.and(
                                                CardType.COMPANION,
                                                Culture.DWARVEN
                                        )
                                )
                        ),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return (gameState.getCurrentSiteNumber() == 7 || gameState.getCurrentSiteNumber() == 8)
										&& gameState.getCurrentSiteBlock() == Block.HOBBIT;
                            }
                        }, -3));
    }

	
    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
				&& PlayConditions.isPhase(game, Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}