package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardFromHandEffect;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDiscardEffect;
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
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Discard 3 cards from hand to play a [MORIA] Orc from your discard
 * pile.
 */
public class Card1_196 extends AbstractLotroCardBlueprint {
    public Card1_196() {
        super(Side.SHADOW, CardType.CONDITION, Culture.MORIA, "They Are Coming");
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
        return 3;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0))
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && game.getGameState().getHand(playerId).size() >= 3) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Discard 3 cards from hand to play a [MORIA] Orc from your discard pile.");
            action.addCost(new ChooseAndDiscardCardFromHandEffect(action, playerId, true));
            action.addCost(new ChooseAndDiscardCardFromHandEffect(action, playerId, true));
            action.addCost(new ChooseAndDiscardCardFromHandEffect(action, playerId, true));
            action.addEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, Filters.and(Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
