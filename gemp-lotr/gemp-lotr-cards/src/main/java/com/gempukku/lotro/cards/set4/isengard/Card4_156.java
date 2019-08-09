package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
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
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make an [ISENGARD] tracker strength +2 (or +4 if skirmishing a character bearing a search card).
 */
public class Card4_156 extends AbstractEvent {
    public Card4_156() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Kill Them Now", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose ISENGARD tracker", Culture.ISENGARD, Keyword.TRACKER) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int bonus =
                                (Filters.inSkirmishAgainst(Filters.hasAttached(Keyword.SEARCH)).accepts(game, card)) ? 4 : 2;
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus)));
                    }
                });
        return action;
    }
}
