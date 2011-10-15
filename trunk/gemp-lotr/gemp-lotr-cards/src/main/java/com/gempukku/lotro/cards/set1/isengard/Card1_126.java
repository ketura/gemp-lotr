package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Maneuver: Make an Uruk-hai fierce until the regroup phase.
 */
public class Card1_126 extends AbstractOldEvent {
    public Card1_126() {
        super(Side.SHADOW, Culture.ISENGARD, "Hunt Them Down!", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose an Uruk-hai", Filters.race(Race.URUK_HAI)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard urukHai) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(urukHai), Keyword.FIERCE), Phase.REGROUP));
                    }
                }
        );
        return action;
    }
}
