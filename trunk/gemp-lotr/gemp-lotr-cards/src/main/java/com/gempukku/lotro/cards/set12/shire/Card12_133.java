package com.gempukku.lotro.cards.set12.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.PreventEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 4
 * Vitality: 4
 * Resistance: 8
 * Game Text: Response: If a burden is about to be added by a Shadow card, spot another Hobbit and exert Tolman Cotton
 * to prevent that.
 */
public class Card12_133 extends AbstractCompanion {
    public Card12_133() {
        super(2, 4, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Tolman Cotton", true);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isAddingBurden(effect, game, Side.SHADOW)
                && PlayConditions.canSpot(game, Filters.not(self), Race.HOBBIT)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new PreventEffect((AddBurdenEffect) effect));
            return Collections.singletonList(action);
        }
        return null;
    }
}
