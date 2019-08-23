package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.AddThreatExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.SpotExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Wraith
 * Strength: 7
 * Vitality: 4
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Enduring. To play, spot a [GONDOR] Wraith and add 2 threats. Response: If Aragorn is about to take a wound
 * in a skirmish, exert King of the Dead to prevent that wound.
 */
public class Card8_038 extends AbstractCompanion {
    public Card8_038() {
        super(4, 7, 4, 6, Culture.GONDOR, Race.WRAITH, Signet.ARAGORN, "King of the Dead", "Oathbreaker", true);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new SpotExtraPlayCostModifier(self, self, null, Culture.GONDOR, Race.WRAITH),
                new AddThreatExtraPlayCostModifier(self, 2, null, self));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.aragorn)
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && PlayConditions.canSelfExert(self, game)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            PhysicalCard aragorn = Filters.findFirstActive(game, Filters.aragorn);
            if (aragorn != null) {
                action.appendEffect(
                        new PreventCardEffect(woundEffect, aragorn));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
