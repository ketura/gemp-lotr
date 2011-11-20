package com.gempukku.lotro.cards.set9.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Vitality: +1
 * Game Text: Bearer must be Elrond or Gil-galad. Skirmish: Discard the top card of your draw deck. Make a minion
 * skirmishing an Elf strength -X, where X is the twilight cost of the discarded card. Discard this artifact.
 */
public class Card9_023 extends AbstractAttachableFPPossession {
    public Card9_023() {
        super(0, 0, 1, Culture.ELVEN, CardType.ARTIFACT, PossessionClass.RING, "Vilya", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Filters.name("Elrond"), Filters.name("Gil-galad"));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, playerId, false) {
                        @Override
                        protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                action.insertEffect(
                                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -card.getBlueprint().getTwilightCost(), CardType.MINION, Filters.inSkirmishAgainst(Race.ELF)));
                            }
                        }
                    });
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
