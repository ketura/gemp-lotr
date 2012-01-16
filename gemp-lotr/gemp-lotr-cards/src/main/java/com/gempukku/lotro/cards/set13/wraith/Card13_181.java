package com.gempukku.lotro.cards.set13.wraith;

import com.gempukku.lotro.cards.*;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: When you play this, you may add a [WRAITH] token here for each [WRAITH] card in your support area.
 * Shadow: Discard this from play or remove 2 tokens from here to play a [WRAITH] mount from your draw deck on your
 * [WRAITH] minion.
 */
public class Card13_181 extends AbstractPermanent {
    public Card13_181() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "They Came From Mordor");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.owner(playerId), Culture.WRAITH, Zone.SUPPORT);
            if (count > 0)
                action.appendEffect(
                        new AddTokenEffect(self, self, Token.WRAITH, count));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new RemoveTokenEffect(self, self, Token.WRAITH, 2));
            possibleCosts.add(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            if (!game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.CANT_PLAY_FROM_DISCARD_OR_DECK)) {
                final Filter additionalAttachmentFilter = Filters.and(Filters.owner(self.getOwner()), Culture.WRAITH, CardType.MINION);

                action.appendEffect(
                        new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDeck(playerId),
                                Filters.and(
                                        Culture.WRAITH, PossessionClass.MOUNT,
                                        ExtraFilters.attachableTo(game, additionalAttachmentFilter)), 0, 1) {
                            @Override
                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                if (selectedCards.size() > 0) {
                                    PhysicalCard selectedCard = selectedCards.iterator().next();
                                    game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, additionalAttachmentFilter, 0));
                                }
                            }
                        });
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
