package com.gempukku.lotro.cards.set21.gundabad;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make an minion strength +2 (or +4 if skirmishing a character bearing a follower).
 */
 
public class Card21_32 extends AbstractEvent {
    public Card21_32() {
        super(Side.SHADOW, 1, Culture.GUNDABAD, "Anger", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a minion") {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int bonus =
                                (Filters.inSkirmishAgainst(Filters.hasAttached(CardType.FOLLOWER)).accepts(game.getGameState(), game.getModifiersQuerying(), card)) ? 4 : 2;
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus)));
                    }
                });
        return action;
    }
}
