package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell. Skirmish: Make Gandalf strength +2 (or +4 if there are 4 or fewer burdens on the Ring-bearer).
 */
public class Card1_078 extends AbstractEvent {
    public Card1_078() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Mysterious Wizard", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        int bonus = (game.getGameState().getBurdens() <= 4) ? 4 : 2;
        final PhysicalCard gandalf = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"));
        if (gandalf != null) {
            action.appendEffect(new CardAffectsCardEffect(self, gandalf));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(gandalf), bonus), Phase.SKIRMISH));
        }
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
