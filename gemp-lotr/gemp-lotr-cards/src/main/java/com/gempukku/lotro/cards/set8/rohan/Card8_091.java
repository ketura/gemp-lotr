package com.gempukku.lotro.cards.set8.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 5
 * Type: Companion • Man
 * Strength: 9
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot 3 [ROHAN] companions. You may play Rohirrim Army any time you could play a regroup event.
 * When you play Rohirrim Army, you may discard a minion.
 */
public class Card8_091 extends AbstractCompanion {
    public Card8_091() {
        super(5, 9, 3, 6, Culture.ROHAN, Race.MAN, null, "Rohirrim Army", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 3, Culture.ROHAN, CardType.COMPANION);
    }

    @Override
    protected Phase getExtraPlayableInPhase() {
        return Phase.REGROUP;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
