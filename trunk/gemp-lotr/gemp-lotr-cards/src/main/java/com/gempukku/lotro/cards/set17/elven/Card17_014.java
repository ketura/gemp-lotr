package com.gempukku.lotro.cards.set17.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event • Archery
 * Game Text: Spot an [ELVEN] archer and add a threat to wound a minion (or add three threats to exert three minions).
 */
public class Card17_014 extends AbstractEvent {
    public Card17_014() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Weapons of Lothlorien", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ELVEN, Keyword.ARCHER)
                && PlayConditions.canAddThreat(game, self, 1);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new AddThreatsEffect(playerId, self, 1) {
                    @Override
                    protected FullEffectResult playEffectReturningResult(LotroGame game) {
                        action.appendEffect(
                                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                        return super.playEffectReturningResult(game);
                    }
                });
        possibleCosts.add(
                new AddThreatsEffect(playerId, self, 3) {
                    @Override
                    protected FullEffectResult playEffectReturningResult(LotroGame game) {
                        action.appendEffect(
                                new ChooseAndExertCharactersEffect(action, playerId, 3, 3, CardType.MINION));
                        return super.playEffectReturningResult(game);
                    }
                });
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));
        return action;
    }
}
