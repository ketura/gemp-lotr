package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot an Uruk-hai. Plays to your support area. Maneuver: Spot 6 companions and remove (2) to
 * wound a companion (except the Ring-bearer).
 */
public class Card1_125 extends AbstractLotroCardBlueprint {
    public Card1_125() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Greed", "1_125");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI))) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
            return Collections.singletonList(action);
        }

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 2)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 6) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.MANEUVER, "Spot 6 companions and remove (2) to wound a companion (except the Ring-bearer).");
            action.addCost(new RemoveTwilightEffect(2));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose non Ring-bearer companion", Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER))) {
                        @Override
                        protected void cardSelected(PhysicalCard companion) {
                            action.addEffect(new WoundCharacterEffect(companion));
                        }
                    }
            );

            return Collections.singletonList(action);
        }
        return null;
    }
}
