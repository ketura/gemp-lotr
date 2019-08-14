package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a [DUNLAND] Man strength +2 (or +4 if you control a site).
 */
public class Card4_005 extends AbstractEvent {
    public Card4_005() {
        super(Side.SHADOW, 0, Culture.DUNLAND, "Burn Every Village", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        final int bonus = (Filters.countActive(game, Filters.siteControlled(playerId)) > 0) ? 4 : 2;
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose DUNLAND Man", Culture.DUNLAND, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus)));
                    }
                });
        return action;
    }
}
