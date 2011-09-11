package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If Ring-bearer puts on The One Ring, exert a Nazgul to add 2 burdens.
 */
public class Card1_228 extends AbstractResponseEvent {
    public Card1_228() {
        super(Side.SHADOW, Culture.WRAITH, "The Twilight World");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING
                && PlayConditions.canPayForShadowCard(game, self, 0)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL), Filters.canExert())) {
            PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new ChooseAndExertCharacterEffect(action, playerId, "Choose a Nazgul", true, Filters.race(Race.NAZGUL), Filters.canExert()));
            action.addEffect(
                    new AddBurdenEffect(game.getGameState().getCurrentPlayerId()));
            action.addEffect(
                    new AddBurdenEffect(game.getGameState().getCurrentPlayerId()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
