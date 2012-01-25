package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Skirmish: Exert Gollum and remove a threat to wound a companion Gollum is skirmishing.
 */
public class Card15_047 extends AbstractPermanent {
    public Card15_047() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Not This Time!");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canExert(self, game, Filters.gollum)
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gollum));
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.inSkirmishAgainst(Filters.gollum)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
