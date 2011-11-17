package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Nazgul is about to take a wound, prevent that wound.
 */
public class Card1_203 extends AbstractResponseOldEvent {
    public Card1_203() {
        super(Side.SHADOW, Culture.WRAITH, "All Blades Perish");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (PlayConditions.isGettingWounded(effect, game, Race.NAZGUL)
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            final WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Nazgul", Race.NAZGUL, Filters.in(woundEffect.getAffectedCardsMinusPrevented(game))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard nazgul) {
                            action.appendEffect(
                                    new PreventCardEffect(woundEffect, nazgul));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
