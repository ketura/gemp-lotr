package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession • Brooch
 * Game Text: To play, spot an Elf. Bearer must be a companion.
 * Response: If another possession borne by bearer is about to be discarded by a Shadow card, discard this possession
 * instead.
 */
public class Card4_063 extends AbstractAttachableFPPossession {
    public Card4_063() {
        super(1, 0, 0, Culture.ELVEN, PossessionClass.BROOCH, "Elven Brooch");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Race.ELF);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return CardType.COMPANION;
    }

    @Override
    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingDiscardedBy(effect, game, Side.SHADOW, CardType.POSSESSION, Filters.not(self), Filters.attachedTo(self.getAttachedTo()))) {
            final DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;
            Collection<PhysicalCard> discardedCards = discardEffect.getAffectedCardsMinusPrevented(game);

            Collection<PhysicalCard> discardedPossesions = Filters.filter(discardedCards, game.getGameState(), game.getModifiersQuerying(),
                    CardType.POSSESSION, Filters.not(self), Filters.attachedTo(self.getAttachedTo()));

            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose possession", Filters.in(discardedPossesions)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new PreventCardEffect(discardEffect, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
