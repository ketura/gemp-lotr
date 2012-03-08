package com.gempukku.lotro.cards.set18.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 0
 * Type: Event • Archery
 * Game Text: Discard a [ROHAN] possession from play to choose one: Wound an unwounded minion; exert a minion;
 * or discard a Shadow card the Ring-bearer is bearing.
 */
public class Card18_101 extends AbstractEvent {
    public Card18_101() {
        super(Side.FREE_PEOPLE, 0, Culture.ROHAN, "Precise Attack", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.ROHAN, CardType.POSSESSION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.ROHAN, CardType.POSSESSION));
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.unwounded) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Wound an unwounded minion";
                    }
                });
        possibleEffects.add(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert a minion";
                    }
                });
        possibleEffects.add(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, Filters.attachedTo(Filters.ringBearer)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard a Shadow card the Ring-bearer is bearing";
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
