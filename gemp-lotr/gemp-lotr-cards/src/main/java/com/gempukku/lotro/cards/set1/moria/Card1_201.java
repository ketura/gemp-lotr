package com.gempukku.lotro.cards.set1.moria;

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
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a [MORIA] Orc strength +2 (or +4 if skirmishing an archer).
 */
public class Card1_201 extends AbstractEvent {
    public Card1_201() {
        super(Side.SHADOW, Culture.MORIA, "Unfamiliar Territory", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(playerId, "Choose a MORIA Orc", Filters.culture(Culture.MORIA), Filters.race(Race.ORC)) {
                    @Override
                    protected void cardSelected(PhysicalCard moriaOrc) {
                        int bonus = 2;
                        Skirmish skirmish = game.getGameState().getSkirmish();
                        if (skirmish != null && skirmish.getFellowshipCharacter() != null
                                && game.getModifiersQuerying().hasKeyword(game.getGameState(), skirmish.getFellowshipCharacter(), Keyword.ARCHER)
                                && skirmish.getShadowCharacters().contains(moriaOrc)) {
                            bonus = 4;
                        }
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(moriaOrc), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
