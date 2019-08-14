package com.gempukku.lotro.cards.set13.gollum;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: Spot Smeagol and discard 2 [GOLLUM] cards from hand to return a minion with strength 6 or less to its
 * owner’s hand. While this is in your discard pile, Smeagol gains muster.
 */
public class Card13_056 extends AbstractEvent {
    public Card13_056() {
        super(Side.FREE_PEOPLE, 1, Culture.GOLLUM, "Softly Up Behind", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.smeagol)
                && PlayConditions.canDiscardCardsFromHandToPlay(self, game, self.getOwner(), 2, Culture.GOLLUM);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2, Culture.GOLLUM));
        action.appendEffect(
                new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, CardType.MINION, Filters.lessStrengthThan(7)));
        return action;
    }

    @Override
    public List<? extends Modifier> getInDiscardModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.smeagol, Keyword.MUSTER));
    }
}
