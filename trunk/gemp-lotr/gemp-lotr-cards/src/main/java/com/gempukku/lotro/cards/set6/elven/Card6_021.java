package com.gempukku.lotro.cards.set6.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession • Ranged Weapon
 * Strength: +1
 * Game Text: Bearer must be an Elf companion. Archery: Exert bearer to discard the top card of your draw deck. If it
 * is an [ELVEN] card, exert a minion.
 */
public class Card6_021 extends AbstractAttachableFPPossession {
    public Card6_021() {
        super(1, 1, 0, Culture.ELVEN, PossessionClass.RANGED_WEAPON, "Naith Longbow");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Race.ELF, CardType.COMPANION);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canExert(self, game, self.getAttachedTo())) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, playerId, false) {
                        @Override
                        protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                            for (PhysicalCard card : cards)
                                if (card.getBlueprint().getCulture() == Culture.ELVEN)
                                    action.appendEffect(
                                            new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
