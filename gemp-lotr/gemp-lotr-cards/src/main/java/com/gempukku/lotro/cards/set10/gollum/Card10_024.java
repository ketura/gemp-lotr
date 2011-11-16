package com.gempukku.lotro.cards.set10.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: While you have initiative, each [GOLLUM] minion is damage +1. Skirmish: Discard this condition to make
 * a [GOLLUM] minion strength +1 for each culture you can spot.
 */
public class Card10_024 extends AbstractPermanent {
    public Card10_024() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Unabated in Malice", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(CardType.MINION, Culture.GOLLUM), new InitiativeCondition(Side.SHADOW), Keyword.DAMAGE, 1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CountCulturesEvaluator(Filters.any), CardType.MINION, Culture.GOLLUM));
            return Collections.singletonList(action);
        }
        return null;
    }
}
