package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
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
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make an Elf or Dwarf skirmishing a [MORIA] Orc strength -1 (or -3 if you spot an Elf and
 * a Dwarf).
 */
public class Card2_069 extends AbstractOldEvent {
    public Card2_069() {
        super(Side.SHADOW, Culture.MORIA, "Old Differences", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose Elf or Dwarf", Filters.inSkirmishAgainst(Filters.or(Filters.race(Race.ELF), Filters.race(Race.DWARF)))) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard elfOrDwarf) {
                        boolean canSpotElf = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF));
                        boolean canSpotDwarf = Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF));
                        int penalty = (canSpotElf && canSpotDwarf) ? -3 : -1;
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(elfOrDwarf), penalty), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
