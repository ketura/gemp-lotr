package com.gempukku.lotro.cards.set9.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: While Boromir is the Ring-bearer, at the start of each skirmish involving him, add 3 burdens or wound him
 * twice. Each time Boromir wins a skirmish, discard each minion he is skirmishing.
 */
public class Card9_031 extends AbstractCompanion {
    public Card9_031() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, null, "Boromir", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && Filters.and(Filters.inSkirmish, Keyword.RING_BEARER).accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new AddBurdenEffect(self, 3));
            possibleEffects.add(
                    new ChooseAndWoundCharactersEffect(action, self.getOwner(), 1, 1, 2, self) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Wound Boromir twice";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, CardType.MINION, Filters.inSkirmishAgainst(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
