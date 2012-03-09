package com.gempukku.lotro.cards.set19.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.CancelEventEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.results.PlayEventResult;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 8
 * Vitality: 4
 * Resistance: 7
 * Game Text: Fellowship. Response: If an event is played, exert Gandalf twice to cancel that event.
 */
public class Card19_008 extends AbstractCompanion {
    public Card19_008() {
        super(4, 8, 4, 7, Culture.GANDALF, Race.WIZARD, null, "Gandalf", true);
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.EVENT)
                && PlayConditions.canSelfExert(self, 2, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new CancelEventEffect(self, (PlayEventResult) effectResult));
            return Collections.singletonList(action);
        }
        return null;
    }
}
