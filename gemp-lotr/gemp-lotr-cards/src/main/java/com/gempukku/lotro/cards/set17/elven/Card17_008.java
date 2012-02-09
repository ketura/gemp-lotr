package com.gempukku.lotro.cards.set17.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Event • Regroup
 * Game Text: Spot an [ELVEN] hunter and choose one: draw a card for each hunter you can spot or reinforce
 * an [ELVEN] token for each hunter you can spot.
 */
public class Card17_008 extends AbstractEvent {
    public Card17_008() {
        super(Side.FREE_PEOPLE, 3, Culture.ELVEN, "Hearth and Hall", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ELVEN, Keyword.HUNTER);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final int count = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Keyword.HUNTER);
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new UnrespondableEffect() {
                    @Override
                    public String getText(LotroGame game) {
                        return "Draw a card for each hunter you can spot";
                    }

                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        action.appendEffect(
                                new DrawCardsEffect(action, playerId, count));
                    }
                });
        possibleEffects.add(
                new UnrespondableEffect() {
                    @Override
                    public String getText(LotroGame game) {
                        return "Reinforce an ELVEN token for each hunter you can spot";
                    }

                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        for (int i = 0; i < count; i++)
                            action.appendEffect(
                                    new ReinforceTokenEffect(self, playerId, Token.ELVEN));
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
