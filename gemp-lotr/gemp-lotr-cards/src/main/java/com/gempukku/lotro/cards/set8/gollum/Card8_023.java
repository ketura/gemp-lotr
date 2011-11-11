package com.gempukku.lotro.cards.set8.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CardLimitEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Possession • Support Area
 * Game Text: To play, spot a [GOLLUM] minion. Regroup: Discard an Orc from hand to add a [GOLLUM] token here.
 * Skirmish: Remove a [GOLLUM] token here to make Shelob strength +3 (limit +6).
 */
public class Card8_023 extends AbstractPermanent {
    public Card8_023() {
        super(Side.SHADOW, 1, CardType.POSSESSION, Culture.GOLLUM, Zone.SUPPORT, "Larder");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.GOLLUM, CardType.MINION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Race.ORC)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Race.ORC));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.GOLLUM));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveTokens(game, Token.GOLLUM, 1, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.GOLLUM));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.name("Shelob"), null,
                                    new CardLimitEvaluator(game, self, Phase.SKIRMISH, 6,
                                            new ConstantEvaluator(3))), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
