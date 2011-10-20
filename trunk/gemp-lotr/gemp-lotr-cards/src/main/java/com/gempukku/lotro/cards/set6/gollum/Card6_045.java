package com.gempukku.lotro.cards.set6.gollum;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Companion
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Ring-bound. To play, add a burden. Skirmish: Discard a card from hand to make Smeagol strength +1.
 */
public class Card6_045 extends AbstractCompanion {
    public Card6_045() {
        super(0, 3, 4, Culture.GOLLUM, null, Signet.FRODO, "Smeagol", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction permanentAction = super.getPlayCardAction(playerId, game, self, twilightModifier);
        permanentAction.appendCost(
                new AddBurdenEffect(self, 1));
        return permanentAction;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && game.getGameState().getHand(playerId).size() >= 1) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 1), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
