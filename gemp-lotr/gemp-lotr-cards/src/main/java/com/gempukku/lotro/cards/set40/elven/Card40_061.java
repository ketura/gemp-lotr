package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Three for the Elven-kings
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Artifact - Support Area
 * Card Number: 1R61
 * Game Text: Fellowship: Stack an [ELVEN] card here.
 * Response: If an [ELVEN] condition is about to be discarded, discard 2 cards from here to prevent that.
 * Regroup: Discard 3 cards from here to heal up to 3 wounds from Elves.
 */
public class Card40_061 extends AbstractPermanent {
    public Card40_061() {
        super(Side.FREE_PEOPLE, 0, CardType.ARTIFACT, Culture.ELVEN, Zone.SUPPORT, "Three for the Elven-kings", null, true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canStackCardFromHand(self, game, playerId, 1, self, Culture.ELVEN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Culture.ELVEN));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 3, self, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 3, 3, self, Filters.any));
            for (int i=0; i<3; i++) {
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, 0, 1, Race.ELF));
            }
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingDiscarded(effect, game, Culture.ELVEN, CardType.CONDITION)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 2, self, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 2, 2, self, Filters.any));
            DiscardCardsFromPlayEffect discardCardsEffect = (DiscardCardsFromPlayEffect) effect;
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, discardCardsEffect, playerId, "Choose condition to save", Culture.ELVEN, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
