package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AddBurdenResult;
import com.gempukku.lotro.logic.timing.results.AddThreatResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Fellowship: Play a [GONDOR] knight. That knight's twilight cost is -2. Each time a Shadow card adds
 * a threat, add a burden. Each time a Shadow card adds a burden, add a threat.
 */
public class Card7_085 extends AbstractCompanion {
    public Card7_085() {
        super(2, 8, 3, Culture.GONDOR, Race.MAN, Signet.ARAGORN, "Denethor", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromHand(playerId, game, -2, Culture.GONDOR, Keyword.KNIGHT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), -2, Culture.GONDOR, Keyword.KNIGHT));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.addedBurden(game, effectResult, Side.SHADOW)) {
            AddBurdenResult burdenResult = (AddBurdenResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(self.getOwner(), self, burdenResult.getCount()));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.addedThreat(game, effectResult, Side.SHADOW)) {
            AddThreatResult threatResult = (AddThreatResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(self, threatResult.getCount()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
