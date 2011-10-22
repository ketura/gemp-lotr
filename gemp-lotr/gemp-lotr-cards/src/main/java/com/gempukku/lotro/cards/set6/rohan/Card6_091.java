package com.gempukku.lotro.cards.set6.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.*;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Skirmish: Discard a mount borne by a [ROHAN] Man to prevent that Man from
 * being overwhelmed unless his or her strength is tripled.
 */
public class Card6_091 extends AbstractPermanent {
    public Card6_091() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Blood Has Been Spilled");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromPlay(self, game, PossessionClass.MOUNT, Filters.attachedTo(Culture.ROHAN, Race.MAN))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, PossessionClass.MOUNT, Filters.attachedTo(Culture.ROHAN, Race.MAN)) {
                        @Override
                        protected void cardsToBeDiscardedCallback(Collection<PhysicalCard> cards) {
                            Set<PhysicalCard> affectedCharacters = new HashSet<PhysicalCard>();
                            for (PhysicalCard card : cards)
                                affectedCharacters.add(card.getAttachedTo());
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OverwhelmedByMultiplierModifier(self, Filters.in(affectedCharacters), 3), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
