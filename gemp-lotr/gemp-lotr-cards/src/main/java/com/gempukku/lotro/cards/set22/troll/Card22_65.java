package com.gempukku.lotro.cards.set22.troll;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CantBeAssignedToSkirmishModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Troll
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: At the start of each assignment phase, you may discard a minion (or exert 
 * a [TROLL] Troll) to spot a [DWARVEN] or [SHIRE] companion. That companion cannot be
 * assigned to a skirmish. Bilbo may exert twice to prevent this.
 */
public class Card22_65 extends AbstractPermanent {
    public Card22_65() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.TROLL, Zone.SUPPORT, "Caught in a Sack");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.ASSIGNMENT)
                && (PlayConditions.canExert(self, game, 1, Culture.TROLL, Race.TROLL) || PlayConditions.canDiscardFromPlay(self, game, CardType.MINION))) {
            final ActivateCardAction action = new ActivateCardAction(self);
			List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 1, Culture.TROLL, Race.TROLL) {
                @Override
                public String getText(LotroGame game) {
                    return "Exert a [TROLL] Troll";
                }
            });
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION) {
                @Override
                public String getText(LotroGame game) {
                    return "Discard a minion";
                }
            });
            action.appendCost(
				new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
					new ChooseActiveCardEffect(self, playerId, "Choose a [DWARVEN] or [SHIRE] companion", CardType.COMPANION, Filters.or(Culture.DWARVEN, Culture.SHIRE), Filters.assignableToSkirmishAgainst(Side.SHADOW, minion)) {
				@Override
				protected void cardSelected(LotroGame game, final PhysicalCard companion) {
					action.appendEffect(new PreventableEffect(
							action,
							new AddUntilEndOfTurnModifierEffect(new CantBeAssignedToSkirmishModifier(self, card)),
							Collections.singletonList(game.getGameState().getCurrentPlayerId()),
							new PreventableEffect.PreventionCost() {
						@Override
						public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
							return new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Bilbo"));
						}
					}));
				}
            });
            return Collections.singletonList(action);
        }
        return null;
	}
}