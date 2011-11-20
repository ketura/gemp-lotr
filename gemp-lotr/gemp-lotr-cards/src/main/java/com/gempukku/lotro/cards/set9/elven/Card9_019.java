package com.gempukku.lotro.cards.set9.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Artifact • Ring
 * Vitality: +1
 * Game Text: Bearer must be Cirdan or Gandalf. Fellowship: Remove X culture tokens, where X is equal to bearer’s
 * vitality. Discard this artifact.
 */
public class Card9_019 extends AbstractAttachableFPPossession {
    public Card9_019() {
        super(1, 0, 1, Culture.ELVEN, CardType.ARTIFACT, PossessionClass.RING, "Narya", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Filters.name("Cirdan"), Filters.gandalf);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            int vitality = game.getModifiersQuerying().getVitality(game.getGameState(), self.getAttachedTo());
            for (int i = 0; i < vitality; i++)
                action.appendEffect(
                        new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, null, 1, Filters.any));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
