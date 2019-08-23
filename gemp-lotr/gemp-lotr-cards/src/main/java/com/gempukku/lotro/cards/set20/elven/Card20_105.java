package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * •Vilya, Elven Ring of Power
 * Elven	Artifact • Ring
 * 1
 * Bearer must be Elrond:
 * Maneuver: Exert Elrond twice to discard a condition borne by a companion.
 */
public class Card20_105 extends AbstractAttachableFPPossession {
    public Card20_105() {
        super(0, 0, 1, Culture.ELVEN, PossessionClass.RING, "Vilya", "Elven Ring of Power", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Elrond");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
            && PlayConditions.canExert(self, game, 2, Filters.name("Elrond"))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Elrond")));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Elrond")));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION, Filters.attachedTo(CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
