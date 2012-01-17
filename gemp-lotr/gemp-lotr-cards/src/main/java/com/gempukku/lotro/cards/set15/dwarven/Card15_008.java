package com.gempukku.lotro.cards.set15.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a Dwarf strength +2 (or if you can spot a hunter minion, strength +1 and damage +1 for each hunter
 * minion you can spot).
 */
public class Card15_008 extends AbstractEvent {
    public Card15_008() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Sturdy Stock", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int count = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.MINION, Keyword.HUNTER);
                        if (count > 0) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, count), Phase.SKIRMISH));
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.DAMAGE, count), Phase.SKIRMISH));
                        } else {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, 2), Phase.SKIRMISH));
                        }
                    }
                });
        return action;
    }
}
