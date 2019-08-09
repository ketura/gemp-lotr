package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make a Dwarf strength +2 (or +4 and damage +1 if bearing 2 hand weapons).
 */
public class Card2_005 extends AbstractEvent {
    public Card2_005() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Flurry of Blows", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard dwarf) {
                        boolean has2Weapons = Filters.countActive(game, Filters.attachedTo(Filters.sameCard(dwarf)), PossessionClass.HAND_WEAPON) == 2;

                        if (has2Weapons) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, Filters.sameCard(dwarf), 4)));
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(new KeywordModifier(self, Filters.sameCard(dwarf), Keyword.DAMAGE)));
                        } else
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, Filters.sameCard(dwarf), 2)));
                    }
                });
        return action;
    }
}
