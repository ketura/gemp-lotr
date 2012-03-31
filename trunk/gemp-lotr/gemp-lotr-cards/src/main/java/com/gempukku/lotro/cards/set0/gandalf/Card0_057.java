package com.gempukku.lotro.cards.set0.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Promotional
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Artifact • Staff
 * Strength: +1
 * Vitality: +1
 * Game Text: Bearer must be Radagast. Each time the fellowship moves during the regroup phase, you may draw 2 cards.
 */
public class Card0_057 extends AbstractAttachableFPPossession {
    public Card0_057() {
        super(1, 1, 1, Culture.GANDALF, CardType.ARTIFACT, PossessionClass.STAFF, "Radagast's Staff", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Radagast");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
                && PlayConditions.isPhase(game, Phase.REGROUP)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
