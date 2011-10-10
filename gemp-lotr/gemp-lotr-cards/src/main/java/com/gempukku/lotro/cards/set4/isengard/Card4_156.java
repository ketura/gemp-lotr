package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make an [ISENGARD] tracker strength +2 (or +4 if skirmishing a character bearing a search card).
 */
public class Card4_156 extends AbstractEvent {
    public Card4_156() {
        super(Side.SHADOW, Culture.ISENGARD, "Kill Them Now", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose ISENGARD tracker", Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER)) {
                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        int bonus =
                                (Filters.inSkirmishAgainst(Filters.hasAttached(Filters.keyword(Keyword.SEARCH))).accepts(game.getGameState(), game.getModifiersQuerying(), card)) ? 4 : 2;
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
