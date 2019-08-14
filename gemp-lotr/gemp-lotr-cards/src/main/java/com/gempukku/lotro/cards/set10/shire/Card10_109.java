package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: Stealth. Spot a [SHIRE] companion and return him or her to your hand to make a Shadow player choose
 * to remove (2) or discard a minion.
 */
public class Card10_109 extends AbstractEvent {
    public Card10_109() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Make Haste", Phase.MANEUVER);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.SHIRE, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, Culture.SHIRE, CardType.COMPANION));
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        List<Effect> possibleEffects = new LinkedList<Effect>();
                        possibleEffects.add(
                                new RemoveTwilightEffect(2));
                        possibleEffects.add(
                                new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, 1, 1, CardType.MINION) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Discard a minion";
                                    }
                                });
                        action.appendEffect(
                                new ChoiceEffect(action, opponentId, possibleEffects));
                    }
                });
        return action;
    }
}
