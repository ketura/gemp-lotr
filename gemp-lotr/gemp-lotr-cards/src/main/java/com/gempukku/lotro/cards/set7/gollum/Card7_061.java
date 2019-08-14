package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.PutPlayedEventOnBottomOfDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Spot Gollum or Smeagol to make a Nazgul, [SAURON] minion, or [GOLLUM] minion strength +2. If you have
 * initiative, you may play this event from your discard pile; place it under your draw deck instead of discarding it.
 */
public class Card7_061 extends AbstractEvent {
    public Card7_061() {
        super(Side.SHADOW, 1, Culture.GOLLUM, "Hobbitses Are Dead", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gollumOrSmeagol);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2,
                        Filters.or(Race.NAZGUL, Filters.and(Culture.SAURON, CardType.MINION), Filters.and(Culture.GOLLUM, CardType.MINION))));
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.hasInitiative(game, Side.SHADOW)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            final PlayEventAction playCardAction = getPlayEventCardAction(playerId, game, self, 0);
            playCardAction.appendEffect(
                    new PutPlayedEventOnBottomOfDeckEffect(playCardAction));
            return Collections.singletonList(playCardAction);
        }
        return null;
    }
}
