package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. To play, spot a [SAURON] Orc. When you play this minion, if you can spot another besieger, you
 * may add a threat for each companion over 4. Shadow: Remove a threat to play a [SAURON] engine from your discard pile.
 */
public class Card8_094 extends AbstractMinion {
    public Card8_094() {
        super(3, 9, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Agitator");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.SAURON, Race.ORC);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canSpot(game, Filters.not(self), Keyword.BESIEGER)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) - 4;
            if (count > 0)
                action.appendEffect(
                        new AddThreatsEffect(playerId, self, count));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canRemoveThreat(game, self, 1)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.SAURON, Keyword.ENGINE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.SAURON, Keyword.ENGINE));
            return Collections.singletonList(action);
        }
        return null;
    }
}
