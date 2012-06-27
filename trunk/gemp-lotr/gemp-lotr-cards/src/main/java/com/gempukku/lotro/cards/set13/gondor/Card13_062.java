package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 3
 * Resistance: 6
 * Game Text: Boromir is resistance -1 for each lurker minion you can spot. Skirmish: Exert Boromir to make him
 * strength +1 for each possession he bears.
 */
public class Card13_062 extends AbstractCompanion {
    public Card13_062() {
        super(3, 8, 3, 6, Culture.GONDOR, Race.MAN, null, "Boromir", "Doomed Heir", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ResistanceModifier(self, self, null, new NegativeEvaluator(new CountActiveEvaluator(CardType.MINION, Keyword.LURKER)));
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            int bonus = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.POSSESSION, Filters.attachedTo(self));
            if (bonus > 0)
                action.appendEffect(
                        new AddUntilEndOfPhaseModifierEffect(
                                new StrengthModifier(self, self, bonus), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
