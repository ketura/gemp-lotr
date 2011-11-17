package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.conditions.InitiativeCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: While you have initiative, each of your [SAURON] minions is strength +2. Skirmish: Discard this condition
 * to make a [SAURON] minion strength +2 (or +4 if it is roaming).
 */
public class Card10_085 extends AbstractPermanent {
    public Card10_085() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Flames Within", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.owner(self.getOwner()), Culture.SAURON, CardType.MINION), new InitiativeCondition(Side.SHADOW), 2));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CardMatchesEvaluator(2, 4, Keyword.ROAMING), Culture.SAURON, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
