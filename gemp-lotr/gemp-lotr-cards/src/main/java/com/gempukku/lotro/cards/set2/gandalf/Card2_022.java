package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Artifact
 * Vitality: +1
 * Game Text: Bearer must be Gandalf. The twilight cost of each [GANDALF] spell is -1. Skirmish: Exert Gandalf twice
 * to cancel a skirmish involving him.
 */
public class Card2_022 extends AbstractAttachableFPPossession {
    public Card2_022() {
        super(2, Culture.GANDALF, CardType.ARTIFACT, Keyword.STAFF, "Gandalf's Staff", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Gandalf");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self,
                Filters.and(
                        Filters.culture(Culture.GANDALF),
                        Filters.keyword(Keyword.SPELL)
                ), -1);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && game.getModifiersQuerying().getVitality(game.getGameState(), self.getAttachedTo()) > 2) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendCost(
                    new ExertCharactersCost(playerId, self.getAttachedTo()));
            action.appendCost(
                    new ExertCharactersCost(playerId, self.getAttachedTo()));

            if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.sameCard(self.getAttachedTo()), Filters.inSkirmish()))
                action.appendEffect(
                        new CancelSkirmishEffect());
            return Collections.singletonList(action);
        }
        return null;
    }
}
