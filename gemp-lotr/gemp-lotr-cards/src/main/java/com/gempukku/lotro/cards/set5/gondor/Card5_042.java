package com.gempukku.lotro.cards.set5.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Knight. To play, spot a knight. Maneuver: Exert Turgon to wound a minion bearing a [GONDOR] fortification.
 */
public class Card5_042 extends AbstractCompanion {
    public Card5_042() {
        super(2, 6, 3, 6, Culture.GONDOR, Race.MAN, null, "Turgon", "Man of Belfalas", true);
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Keyword.KNIGHT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION, Filters.hasAttached(Culture.GONDOR, Keyword.FORTIFICATION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
