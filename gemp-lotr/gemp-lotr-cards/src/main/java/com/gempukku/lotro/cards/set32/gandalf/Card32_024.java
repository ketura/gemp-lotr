package com.gempukku.lotro.cards.set32.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CardPhaseLimitEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * •Saruman, The White
 * Ally • Home 3 • Wizard
 * Twilight Cost 4
 * Strength 8
 * Vitality 4
 * Wise.
 * Game Text: Wise. Gandalf is defender +1. Fellowship: Discard a Wise ally to play Gandalf from your
 * discard pile. Skirmish: Exert Saruman to make a Wise character strength +2 (limit +2).
 */
public class Card32_024 extends AbstractAlly {
    public Card32_024() {
        super(4, SitesBlock.HOBBIT, 3, 8, 4, Race.WIZARD, Culture.GANDALF, "Saruman", "The White", true);
        addKeyword(Keyword.WISE);
    }
    
    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.gandalf, Keyword.DEFENDER, 1));
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.ALLY, Keyword.WISE)
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.name("Gandalf"))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.ALLY, Keyword.WISE));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.name("Gandalf")));
            actions.add(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Wise character", Keyword.WISE) {
                @Override
                public void cardSelected(LotroGame game, PhysicalCard card) {
                    action.insertEffect(
                            new AddUntilEndOfPhaseModifierEffect(
                                    new StrengthModifier(self, Filters.sameCard(card), null,
                                            new CardPhaseLimitEvaluator(game, self, Phase.SKIRMISH, 2, new ConstantEvaluator(2)))));
                }
            });
            actions.add(action);
        }
        return actions;
    }
}
