package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collection;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Discard any number of [GOLLUM] conditions. For each condition discarded, make Smeagol strength +3
 * and damage +1.
 */
public class Card7_054 extends AbstractEvent {
    public Card7_054() {
        super(Side.FREE_PEOPLE, 2, Culture.GOLLUM, "Clever Hobbits", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, Integer.MAX_VALUE, Culture.GOLLUM, CardType.CONDITION) {
                    @Override
                    protected void forEachDiscardedByEffectCallback(Collection<PhysicalCard> cards) {
                        int count = cards.size();
                        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                new StrengthModifier(self, Filters.smeagol, count * 3), Phase.SKIRMISH);
                        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                new KeywordModifier(self, Filters.smeagol, Keyword.DAMAGE, count), Phase.SKIRMISH);
                    }
                });
        return action;
    }
}
