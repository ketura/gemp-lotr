package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition
 * Vitality: +1
 * Game Text: Stealth. Bearer must be an unbound Hobbit. Limit 1 per character. Skirmish: Exert bearer twice to cancel
 * a skirmish involving him. Any Shadow player may remove (1) to prevent this.
 */
public class Card4_300 extends AbstractAttachable {
    public Card4_300() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 0, Culture.SHIRE, null, "Escape");
        addKeyword(Keyword.STEALTH);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.HOBBIT, Filters.unboundCompanion, Filters.not(Filters.hasAttached(Filters.name("Escape"))));
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 2, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new PreventableEffect(action,
                            new CancelSkirmishEffect(Filters.hasAttached(self)),
                            GameUtils.getOpponents(game, playerId),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new RemoveTwilightEffect(1);
                                }
                            }
                    ));
            return Collections.singletonList(action);
        }
        return null;
    }
}
