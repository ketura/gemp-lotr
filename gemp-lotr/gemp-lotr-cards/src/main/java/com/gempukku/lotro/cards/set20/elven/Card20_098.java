package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardsFromHandOnTopOfDrawDeckEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * •Nenya, Ring of Adamant
 * Elven	Artifact • Ring
 * 1
 * Bearer must be Galadriel.
 * Manuever: Heal 2 companions and place up to 2 cards from hand on top of your draw deck. Discard this Artifact.
 */
public class Card20_098 extends AbstractAttachableFPPossession{
    public Card20_098() {
        super(0, 0, 1, Culture.ELVEN, PossessionClass.RING, "Nenya", "Ring of Adamant", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.galadriel;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 2, 2, CardType.COMPANION));
            action.appendEffect(
                    new ChooseAndPutCardsFromHandOnTopOfDrawDeckEffect(action, playerId, 0, 2, Filters.any));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
