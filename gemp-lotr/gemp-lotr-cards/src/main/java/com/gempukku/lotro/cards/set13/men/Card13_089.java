package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 7
 * Vitality: 2
 * Site: 4
 * Game Text: Lurker. (Skirmishes involving lurker minions must be resolved before any others.) To play, spot a [MEN]
 * minion. Assignment: Discard a [MEN] card from your hand to assign this minion to an unbound companion.
 */
public class Card13_089 extends AbstractMinion {
    public Card13_089() {
        super(3, 7, 2, 4, Race.MAN, Culture.MEN, "Dunlending Trapper");
        addKeyword(Keyword.LURKER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.MEN, CardType.MINION);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.MEN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.MEN));
            action.appendEffect(
                    new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
