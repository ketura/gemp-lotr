package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 2
 * Arrows of Light
 * Elven	Event • Archery
 * To play, spot an Elf archer companion.
 * Make the fellowship archery total -X to discard X Shadow conditions, where X is the number of archer companions you can spot.
 */
public class Card20_072 extends AbstractEvent {
    public Card20_072() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Arrows of Light", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.ELF, CardType.COMPANION, Keyword.ARCHER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        int x = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Keyword.ARCHER);
        action.appendCost(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -x), Phase.ARCHERY));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, x, x, CardType.CONDITION, Side.SHADOW));
        return action;
    }
}
