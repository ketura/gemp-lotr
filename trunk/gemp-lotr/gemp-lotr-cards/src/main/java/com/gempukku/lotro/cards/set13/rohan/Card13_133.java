package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ReplaceSiteResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 5
 * Game Text: When you play this, you may reinforce a [ROHAN] token. Each time a Shadow player replaces the fellowship’s
 * current site, you may spot another [ROHAN] Man to make, until the regroup phase, the fellowship archery total +2
 * and the minion archery total -2.
 */
public class Card13_133 extends AbstractCompanion {
    public Card13_133() {
        super(2, 6, 3, 5, Culture.ROHAN, Race.MAN, null, "Riddermark Tactician");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.ROHAN));
            return Collections.singletonList(action);
        }
        if (effectResult.getType() == EffectResult.Type.REPLACE_SITE
                && PlayConditions.canSpot(game, Filters.not(self), Culture.ROHAN, Race.MAN)) {
            ReplaceSiteResult replaceSiteResult = (ReplaceSiteResult) effectResult;
            if (replaceSiteResult.getSiteNumber() == game.getGameState().getCurrentSiteNumber()
                    && !replaceSiteResult.getPlayerId().equals(game.getGameState().getCurrentPlayerId())) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new AddUntilStartOfPhaseModifierEffect(
                                new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 2), Phase.REGROUP));
                action.appendEffect(
                        new AddUntilStartOfPhaseModifierEffect(
                                new ArcheryTotalModifier(self, Side.SHADOW, -2), Phase.REGROUP));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
