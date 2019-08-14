package com.gempukku.lotro.cards.set13.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, exert 2 Dwarves. When you play this, add a [DWARVEN] token here for each mountain site and each
 * underground site on the adventure path. Skirmish: Discard this from play or remove 2 tokens from here to make
 * a Shadow player discard the top card of his or her draw deck.
 */
public class Card13_008 extends AbstractPermanent {
    public Card13_008() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, "Subterranean Homestead", null, true);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, 2, Race.DWARF));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countActive(game, CardType.SITE, Zone.ADVENTURE_PATH, Filters.or(Keyword.MOUNTAIN, Keyword.UNDERGROUND));
            if (count > 0)
                action.appendEffect(
                        new AddTokenEffect(self, self, Token.DWARVEN, count));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && (PlayConditions.canRemoveTokens(game, self, Token.DWARVEN, 2) || PlayConditions.canSelfDiscard(self, game))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new RemoveTokenEffect(self, self, Token.DWARVEN, 2));
            possibleCosts.add(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new DiscardTopCardFromDeckEffect(self, opponentId, true));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
