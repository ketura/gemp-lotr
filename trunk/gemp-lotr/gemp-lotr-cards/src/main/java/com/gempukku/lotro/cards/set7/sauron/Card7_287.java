package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: Skirmish: Exert 3 [SAURON] Orcs to add 3 threats. When this minion is discarded, remove 3 threats.
 */
public class Card7_287 extends AbstractMinion {
    public Card7_287() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Mordor Guard");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canExertMultiple(self, game, 1, 3, Culture.SAURON, Race.ORC)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 3, 3, Culture.SAURON, Race.ORC));
            action.appendEffect(
                    new AddThreatsEffect(playerId, self, 3));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public RequiredTriggerAction getDiscardedFromPlayRequiredTrigger(LotroGame game, PhysicalCard self) {
        RequiredTriggerAction action = new RequiredTriggerAction(self);
        action.appendEffect(
                new RemoveThreatsEffect(self, 3));
        return action;
    }
}
