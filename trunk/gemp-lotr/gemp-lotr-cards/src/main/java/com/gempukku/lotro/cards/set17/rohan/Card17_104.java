package com.gempukku.lotro.cards.set17.rohan;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.CantBeTransferredModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Possessions cannot be transferred from this companion to another character. Fellowship: Play a possession
 * on this companion to liberate a site.
 */
public class Card17_104 extends AbstractCompanion {
    public Card17_104() {
        super(3, 7, 3, 6, Culture.ROHAN, Race.MAN, null, "Warrior of Rohan");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantBeTransferredModifier(self, Filters.and(CardType.POSSESSION, Filters.attachedTo(self)));
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromHand(playerId, game, CardType.POSSESSION, ExtraFilters.attachableTo(game, self))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseCardsFromHandEffect(playerId, 1, 1, CardType.POSSESSION, ExtraFilters.attachableTo(game, self)) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards) {
                                AttachPermanentAction attachPermanentAction = ((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, Filters.and(self), 0);
                                game.getActionsEnvironment().addActionToStack(attachPermanentAction);
                                action.appendEffect(
                                        new LiberateASiteEffect(self));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
