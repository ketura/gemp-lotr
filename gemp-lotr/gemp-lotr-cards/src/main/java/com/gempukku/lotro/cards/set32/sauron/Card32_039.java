package com.gempukku.lotro.cards.set32.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.CantPlayCardsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Regroup: Discard a minion to stack a Wise character on this condition. A Wise character
 * may exert twice to prevent this. Copies of characters stacked here cannot be played.
 */
public class Card32_039 extends AbstractPermanent {
    public Card32_039() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, "Jail");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(self);
        for (PhysicalCard stackedCard : stackedCards) {
            modifiers.add(
                    new CantPlayCardsModifier(self, Filters.name(stackedCard.getBlueprint().getTitle())));
        }
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && Filters.countActive(game, CardType.MINION) > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION));
            action.appendEffect(new ChooseActiveCardEffect(self, playerId, "Choose a Wise character",
                    Keyword.WISE) {
                @Override
                public void cardSelected(final LotroGame game, final PhysicalCard wiseCharacter) {
                    action.insertEffect(new PreventableEffect(action,
                            new StackCardFromPlayEffect(wiseCharacter, self), game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                    return new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, 2, Keyword.WISE) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Exert a Wise character twice";
                                        }
                                    };
                                }
                            }));
                }
            });
            return Collections.singletonList(action);
        }
        return null;
    }
}
