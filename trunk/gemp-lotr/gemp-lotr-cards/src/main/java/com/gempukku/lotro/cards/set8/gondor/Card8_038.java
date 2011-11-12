package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

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
        super(4, 7, 4, Culture.GONDOR, Race.WRAITH, Signet.ARAGORN, "King of the Dead", true);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.GONDOR, Race.WRAITH)
                && PlayConditions.canAddThreat(game, self, 2);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayPermanentAction permanentAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        permanentAction.appendCost(
                new AddThreatsEffect(playerId, self, 2));
        return permanentAction;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.isGettingWounded(effect, game, Filters.aragorn)
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && PlayConditions.canSelfExert(self, game)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            PhysicalCard aragorn = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.aragorn);
            if (aragorn != null) {
                action.appendEffect(
                        new PreventCardEffect(woundEffect, aragorn));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
