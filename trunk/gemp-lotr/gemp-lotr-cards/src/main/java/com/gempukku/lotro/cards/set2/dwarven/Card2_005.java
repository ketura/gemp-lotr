package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

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
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Flurry of Blows", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose a Dwarf", Filters.race(Race.DWARF)) {
                    @Override
                    protected void cardSelected(PhysicalCard dwarf) {
                        boolean has2Weapons = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.attachedTo(Filters.sameCard(dwarf)), Filters.keyword(Keyword.HAND_WEAPON)) == 2;

                        Modifier modifier;
                        if (has2Weapons) {
                            List<Modifier> modifiers = new LinkedList<Modifier>();
                            modifiers.add(
                                    new StrengthModifier(null, null, 4));
                            modifiers.add(
                                    new KeywordModifier(null, null, Keyword.DAMAGE));

                            modifier = new CompositeModifier(self, Filters.sameCard(dwarf), modifiers);
                        } else
                            modifier = new StrengthModifier(self, Filters.sameCard(dwarf), 2);
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(modifier, Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
