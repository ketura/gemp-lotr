package com.gempukku.lotro.cards.set4.raider;

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
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Skirmish: Make a [RAIDER] Man strength +3 (or +4 if you spot 4 burdens).
 */
public class Card4_239 extends AbstractOldEvent {
    public Card4_239() {
        super(Side.SHADOW, Culture.RAIDER, "Men of Rhun", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose RAIDER Man", Culture.RAIDER, Filters.race(Race.MAN)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int bonus = (game.getGameState().getBurdens() >= 4) ? 4 : 3;
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
