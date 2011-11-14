package com.gempukku.lotro.cards.set8.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: When you play a minion stacked here, you may exert a companion. Regroup: If no minion is stacked here,
 * spot your [WRAITH] minon to stack it here. Shadow: Spot 2 [WRAITH] minions to play a [WRAITH] minion stacked here as
 * if played from hand.
 */
public class Card8_071 extends AbstractPermanent {
    public Card8_071() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Flung Into the Fray", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, CardType.MINION)) {
            PlayCardResult playCardResult = (PlayCardResult) effectResult;
            if (playCardResult.getPlayedFrom() == Zone.STACKED && playCardResult.getAttachedOrStackedPlayedFrom() == self) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION));
                return Collections.singletonList(action);
            }
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && Filters.filter(game.getGameState().getStackedCards(self), game.getGameState(), game.getModifiersQuerying(), CardType.MINION).size() == 0
                && PlayConditions.canSpot(game, Filters.owner(playerId), Culture.WRAITH, CardType.MINION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose your WRAITH minion", Filters.owner(playerId), Culture.WRAITH, CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new StackCardFromPlayEffect(card, self));
                        }
                    });
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, 2, Culture.WRAITH, CardType.MINION)
                && PlayConditions.canPlayFromStacked(playerId, game, self, Culture.WRAITH, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, Culture.WRAITH, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
