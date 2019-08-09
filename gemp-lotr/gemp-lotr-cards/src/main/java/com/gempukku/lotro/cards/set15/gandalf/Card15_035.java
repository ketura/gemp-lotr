package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.KillEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 6
 * Type: Companion • Ent
 * Strength: 8
 * Vitality: 3
 * Resistance: 6
 * Game Text: This companion is twilight cost -1 for each [GANDALF] companion you can spot. Fellowship: Place this
 * companion in the dead pile to remove (5).
 */
public class Card15_035 extends AbstractCompanion {
    public Card15_035() {
        super(6, 8, 3, 6, Culture.GANDALF, Race.ENT, null, "Shadow of the Wood");
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -Filters.countActive(game, Culture.GANDALF, CardType.COMPANION);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new KillEffect(self, KillEffect.Cause.CARD_EFFECT));
            action.appendEffect(
                    new RemoveTwilightEffect(5));
            return Collections.singletonList(action);
        }
        return null;
    }
}
