package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.Collections;

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
        super(Side.SHADOW, Culture.ISENGARD, "Brought Back Alive", Phase.ASSIGNMENT);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game, Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER), Filters.notAssignedToSkirmish(), Filters.canBeAssignedToSkirmish(Side.SHADOW)) {
                    @Override
                    protected void forEachCardExertedCallback(final PhysicalCard minion) {
                        action.appendEffect(
                                new ChooseActiveCardEffect(self, playerId, "Choose an unbound companion", Filters.unboundCompanion(), Filters.notAssignedToSkirmish(), Filters.canBeAssignedToSkirmish(Side.SHADOW)) {
                                    @Override
                                    protected void cardSelected(PhysicalCard companion) {
                                        Race race = companion.getBlueprint().getRace();
                                        AssignmentEffect assignmentEffect = new AssignmentEffect(playerId, companion, Collections.singletonList(minion));
                                        if (race == Race.HOBBIT) {
                                            action.insertEffect(
                                                    assignmentEffect);
                                        } else {
                                            action.insertEffect(
                                                    new PreventableEffect(action,
                                                            assignmentEffect, game.getGameState().getCurrentPlayerId(),
                                                            new ExertCharactersEffect(self, companion)));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
