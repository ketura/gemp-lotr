package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Make an [URUK-HAI] minion strength +2 and damage +1.
 */
public class Card18_117 extends AbstractEvent {
    public Card18_117() {
        super(Side.SHADOW, 2, Culture.URUK_HAI, "Ghastly Wound", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an URUK_HAI minion", Culture.URUK_HAI, CardType.MINION) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, 2), Phase.SKIRMISH));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, card, Keyword.DAMAGE, 1), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
