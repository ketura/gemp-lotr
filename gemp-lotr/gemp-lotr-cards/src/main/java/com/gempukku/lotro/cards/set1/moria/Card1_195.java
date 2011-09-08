package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Remove (2) to play a [MORIA] possession from your discard pile.
 */
public class Card1_195 extends AbstractLotroCardBlueprint {
    public Card1_195() {
        super(Side.SHADOW, CardType.CONDITION, Culture.MORIA, "Relics of Moria");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier);
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT, twilightModifier);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0))
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 2)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SHADOW, "Remove (2) to play a [MORIA] possession from your discard pile.");
            action.addCost(new RemoveTwilightEffect(2));
            action.addEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, Filters.and(Filters.culture(Culture.MORIA), Filters.type(CardType.POSSESSION))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
