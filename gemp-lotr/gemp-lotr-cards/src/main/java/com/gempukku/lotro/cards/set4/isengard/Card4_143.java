package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Search. Assignment: Exert an [ISENGARD] tracker to assign it to an unbound companion. That companion may
 * exert to prevent this (unless that companion is a Hobbit).
 */
public class Card4_143 extends AbstractEvent {
    public Card4_143() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Brought Back Alive", Phase.ASSIGNMENT);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.ISENGARD, Keyword.TRACKER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ISENGARD, Keyword.TRACKER) {
                    @Override
                    protected void forEachCardExertedCallback(final PhysicalCard minion) {
                        action.appendEffect(
                                new ChooseActiveCardEffect(self, playerId, "Choose an unbound companion", Filters.unboundCompanion, Filters.assignableToSkirmishAgainst(Side.SHADOW, minion)) {
                                    @Override
                                    protected void cardSelected(LotroGame game, final PhysicalCard companion) {
                                        Race race = companion.getBlueprint().getRace();
                                        AssignmentEffect assignmentEffect = new AssignmentEffect(playerId, companion, minion);
                                        if (race == Race.HOBBIT) {
                                            action.insertEffect(
                                                    assignmentEffect);
                                        } else {
                                            action.insertEffect(
                                                    new PreventableEffect(action,
                                                            assignmentEffect, game.getGameState().getCurrentPlayerId(),
                                                            new PreventableEffect.PreventionCost() {
                                                                @Override
                                                                public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                                                    return new ExertCharactersEffect(action, self, companion);
                                                                }
                                                            }
                                                    ));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
