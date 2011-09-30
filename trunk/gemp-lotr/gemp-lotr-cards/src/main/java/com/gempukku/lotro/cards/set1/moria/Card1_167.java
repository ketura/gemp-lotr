package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make a [MORIA] Orc strength +1 for each other [MORIA] Orc you spot (limit +4).
 */
public class Card1_167 extends AbstractEvent {
    public Card1_167() {
        super(Side.SHADOW, Culture.MORIA, "Denizens Enraged", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose MORIA Orc", Filters.culture(Culture.MORIA), Filters.race(Race.ORC)) {
                    @Override
                    protected void cardSelected(PhysicalCard orc) {
                        int bonus = Math.min(4, Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.race(Race.ORC), Filters.not(Filters.sameCard(orc))));
                        action.appendEffect(new CardAffectsCardEffect(self, orc));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(orc), bonus), Phase.SKIRMISH));
                    }
                }
        );

        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
