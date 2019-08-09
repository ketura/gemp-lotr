package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * His Wrath Was Redoubled [Gandalf]
 * Event • Assignment
 * Twilight Cost 3
 * 'Spell.
 * Discard Beorn (or 2 [Dwarven] followers) to make a Shadow player assign 3 wounds to minions (except Smaug).'
 */
public class Card31_016 extends AbstractEvent {
    public Card31_016() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "His Wrath Was Redoubled", Phase.ASSIGNMENT);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && (PlayConditions.canDiscardFromPlay(self, game, Filters.name("Beorn"))
                || PlayConditions.canDiscardFromPlay(self, game, 2, Culture.DWARVEN, CardType.FOLLOWER));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.name("Beorn")) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard Beorn";
                    }
                });
        possibleCosts.add(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Culture.DWARVEN, CardType.FOLLOWER) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Discard 2 [DWARVEN] followers";
                    }
                });

        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));

        action.appendEffect(
                new ChooseOpponentEffect(self.getOwner()) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        action.appendEffect(
                                new ChooseAndWoundCharactersEffect(action, opponentId, 1, 1, CardType.MINION, Filters.not(Filters.name("Smaug"))));
                        action.appendEffect(
                                new ChooseAndWoundCharactersEffect(action, opponentId, 1, 1, CardType.MINION, Filters.not(Filters.name("Smaug"))));
                        action.appendEffect(
                                new ChooseAndWoundCharactersEffect(action, opponentId, 1, 1, CardType.MINION, Filters.not(Filters.name("Smaug"))));
                    }
                });

        return action;
    }
}
