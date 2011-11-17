package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Southron. Archery: Spot another [RAIDER] Man, spot 6 companions, and remove a threat to exert a companion.
 */
public class Card7_168 extends AbstractMinion {
    public Card7_168() {
        super(3, 8, 2, 4, Race.MAN, Culture.RAIDER, "Southron Thief");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.RAIDER, Race.MAN)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
