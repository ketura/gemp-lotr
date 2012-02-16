package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RevealCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardsFromHandBeneathDrawDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Each time a mounted [ORC] Orc wins a skirmish, the Free Peoples player must reveal his or her hand
 * and place two Free Peoples cards revealed on the bottom of his or her draw deck.
 */
public class Card17_069 extends AbstractPermanent {
    public Card17_069() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "Cry and Panic");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.ORC, Race.ORC, Filters.mounted)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new RevealCardsFromHandEffect(self, game.getGameState().getCurrentPlayerId(), new HashSet<PhysicalCard>(game.getGameState().getHand(game.getGameState().getCurrentPlayerId()))));
            action.appendEffect(
                    new ChooseAndPutCardsFromHandBeneathDrawDeckEffect(action, game.getGameState().getCurrentPlayerId(), 2, Side.FREE_PEOPLE));
            return Collections.singletonList(action);
        }
        return null;
    }
}
