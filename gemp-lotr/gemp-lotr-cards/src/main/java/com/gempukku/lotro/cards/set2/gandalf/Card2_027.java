package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Spell. Skirmish: Discard a staff Gandalf is bearing and then exert him twice to discard a minion he
 * is skirmishing.
 */
public class Card2_027 extends AbstractOldEvent {
    public Card2_027() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Staff Asunder", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canDiscardFromPlay(self, game, PossessionClass.STAFF, Filters.attachedTo(Filters.gandalf))
                && PlayConditions.canExert(self, game, 2, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, PossessionClass.STAFF, Filters.attachedTo(Filters.gandalf)));
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));

        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.gandalf, Filters.inSkirmish))
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(
                            action, playerId, 1, 1, CardType.MINION, Filters.inSkirmish));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
