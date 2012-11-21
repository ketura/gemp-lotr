package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.RevealAndChooseCardsFromOpponentHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Artifact • Palantir
 * Resistance: -2
 * Game Text: Bearer must be Gandalf. To play this, if you can spot The Palantir of Orthanc, discard it. At the start of
 * the fellowship phase, you may add a burden to reveal a Shadow player's hand. He or she must choose a revealed minion
 * and discard it from hand.
 */
public class Card13_036 extends AbstractAttachableFPPossession {
    public Card13_036() {
        super(0, 0, 0, Culture.GANDALF, CardType.ARTIFACT, PossessionClass.PALANTIR, "The Palantir of Orthanc", "Recovered Seeing Stone", true);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new ResistanceModifier(self, Filters.hasAttached(self), -2));
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    protected boolean skipUniquenessCheck() {
        return true;
    }

    @Override
    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filterable additionalAttachmentFilter, int twilightModifier) {
        AttachPermanentAction permanentAction = super.getPlayCardAction(playerId, game, self, additionalAttachmentFilter, twilightModifier);
        if (PlayConditions.canSpot(game, Filters.name(getName())))
            permanentAction.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(permanentAction, playerId, 1, 1, Filters.name(getName())));
        return permanentAction;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new AddBurdenEffect(self.getOwner(), self, 1));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(final String opponentId) {
                            action.appendEffect(
                                    new RevealAndChooseCardsFromOpponentHandEffect(action, playerId, opponentId, self, "Opponent's hand", Filters.none, 0, 0) {
                                        @Override
                                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                            action.appendEffect(
                                                    new ChooseAndDiscardCardsFromHandEffect(action, opponentId, true, 1, CardType.MINION));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
