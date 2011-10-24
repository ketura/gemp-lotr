package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make an [ISENGARD] Orc strength +2 (and heal it if mounted).
 */
public class Card5_068 extends AbstractEvent {
    public Card5_068() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Wolf-voices", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose ISENGARD Orc", Culture.ISENGARD, Race.ORC) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        boolean isMounted = Filters.mounted.accepts(game.getGameState(), game.getModifiersQuerying(), card);
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), 2), Phase.SKIRMISH));
                        if (isMounted)
                            action.appendEffect(
                                    new HealCharactersEffect(self, card));
                    }
                });
        return action;
    }
}
