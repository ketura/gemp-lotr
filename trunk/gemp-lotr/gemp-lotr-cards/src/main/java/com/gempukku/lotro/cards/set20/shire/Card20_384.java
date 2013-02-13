package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Don't Be Hasty
 * Shire	Event • Manuever
 * Tale.
 * Exert a Hobbit companion and spot X pipes to make a shadow player choose to remove (X) or discard a minion.
 */
public class Card20_384 extends AbstractEvent {
    public Card20_384() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Don't Be Hasty", Phase.MANEUVER);
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.HOBBIT, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, CardType.COMPANION));
        action.appendCost(
                new ForEachYouSpotEffect(playerId, PossessionClass.PIPE) {
                    @Override
                    protected void spottedCards(final int spotCount) {
                        action.appendEffect(
                                new ChooseOpponentEffect(playerId) {
                                    @Override
                                    protected void opponentChosen(String opponentId) {
                                        List<Effect> possibleEffects = new LinkedList<Effect>();
                                        possibleEffects.add(
                                                new RemoveTwilightEffect(spotCount));
                                        possibleEffects.add(
                                                new ChooseAndDiscardCardsFromPlayEffect(action, opponentId,1, 1, CardType.MINION) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Discard a minion";
                                                    }
                                                });
                                        action.appendEffect(
                                                new ChoiceEffect(action, opponentId, possibleEffects));
                                    }
                                });
                    }
                });
        return action;
    }
}
