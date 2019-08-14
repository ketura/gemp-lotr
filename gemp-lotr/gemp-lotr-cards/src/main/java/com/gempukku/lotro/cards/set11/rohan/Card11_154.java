package com.gempukku.lotro.cards.set11.rohan;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExhaustCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 5
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot 2 [ROHAN] companions. You may play Riders of the Mark any time you could play a skirmish
 * event. When you play Riders of the Mark, you may exhaust a minion.
 */
public class Card11_154 extends AbstractCompanion {
    public Card11_154() {
        super(5, 8, 3, 6, Culture.ROHAN, Race.MAN, null, "Riders of the Mark", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Culture.ROHAN, CardType.COMPANION);
    }

    @Override
    public List<? extends Action> getPhaseActionsInHand(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.canPlayFromHand(playerId, game, self)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            return Collections.singletonList(
                    PlayUtils.getPlayCardAction(game, self, 0, Filters.any, false));
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExhaustCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
