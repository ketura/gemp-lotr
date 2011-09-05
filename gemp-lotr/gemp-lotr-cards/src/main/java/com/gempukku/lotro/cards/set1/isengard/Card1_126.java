package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Maneuver: Make an Uruk-hai fierce until the regroup phase.
 */
public class Card1_126 extends AbstractEvent {
    public Card1_126() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Hunt Them Down!", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.keyword(Keyword.URUK_HAI)) {
                    @Override
                    protected void cardSelected(PhysicalCard urukHai) {
                        action.addEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(urukHai), Keyword.FIERCE), Phase.REGROUP));
                    }
                }
        );
        return action;
    }
}
