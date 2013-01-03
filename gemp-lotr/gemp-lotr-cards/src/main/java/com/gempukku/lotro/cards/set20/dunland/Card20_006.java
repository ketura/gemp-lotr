package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * Dunlending Clansman
 * Dunland	Minion • Man
 * 11	1	3
 * When you play Dunlending Clansman, you may discard a [Dunland] card from hand. If you do, Dunlending Clansman
 * may not take wounds until the regroup phase.
 */
public class Card20_006 extends AbstractMinion {
    public Card20_006() {
        super(4, 11, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Clansman");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.DUNLAND)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.DUNLAND));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new CantTakeWoundsModifier(self, self), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
