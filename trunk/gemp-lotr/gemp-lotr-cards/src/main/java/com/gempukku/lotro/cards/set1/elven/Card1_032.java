package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make an Elf strength +2 (or +4 if skirmishing an archer).
 */
public class Card1_032 extends AbstractEvent {
    public Card1_032() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Border Defenses", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);

        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an Elf", Filters.race(Race.ELF)) {
                    @Override
                    protected void cardSelected(PhysicalCard elf) {
                        Skirmish skirmish = game.getGameState().getSkirmish();
                        int bonus = 2;
                        if (skirmish != null) {
                            if (skirmish.getFellowshipCharacter() == elf && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.MINION), Filters.keyword(Keyword.ARCHER)))
                                bonus = 4;
                        }

                        action.appendEffect(new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, Filters.sameCard(elf), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
