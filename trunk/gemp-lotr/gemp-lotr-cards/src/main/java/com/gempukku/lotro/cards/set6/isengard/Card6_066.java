package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardStackedCardsEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 3
 * Site: 4
 * Game Text: Regroup: If stacked on an [ISENGARD] card, discard this card to add (2).
 */
public class Card6_066 extends AbstractMinion {
    public Card6_066() {
        super(3, 7, 3, 4, Race.ORC, Culture.ISENGARD, "Isengard Builder");
    }

    @Override
    public List<? extends Action> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseStackedShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && self.getStackedOn().getBlueprint().getCulture() == Culture.ISENGARD) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardStackedCardsEffect(self, Collections.singleton(self)));
            action.appendEffect(
                    new AddTwilightEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
