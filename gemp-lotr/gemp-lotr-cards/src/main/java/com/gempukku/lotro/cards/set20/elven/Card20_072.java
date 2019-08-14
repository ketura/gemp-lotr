package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF, CardType.COMPANION, Keyword.ARCHER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        int x = Filters.countActive(game, CardType.COMPANION, Keyword.ARCHER);
        action.appendCost(
                new AddUntilEndOfPhaseModifierEffect(
                        new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -x)));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, x, x, CardType.CONDITION, Side.SHADOW));
        return action;
    }
}
