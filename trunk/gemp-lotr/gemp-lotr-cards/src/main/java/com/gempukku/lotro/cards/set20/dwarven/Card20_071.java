package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.results.PlayEventResult;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Well Prepared
 * Dwarven	Condition • Support Area
 * While you can spot 3 cards stacked on this condition, each time a Shadow event is played, the Shadow player must exert a minion.
 */
public class Card20_071 extends AbstractPermanent {
    public Card20_071() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Well Prepared", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Side.SHADOW, CardType.EVENT)
                && game.getGameState().getStackedCards(self).size()>=3) {
            PlayEventResult playEventResult = (PlayEventResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playEventResult.getPlayEventAction().getPerformingPlayer(), 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
