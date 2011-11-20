package com.gempukku.lotro.cards.set9.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
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
 * Game Text: While Isildur is the Ring-bearer, at the start of each skirmish involving him, add 2 burdens or exert
 * 3 companions. While Isildur bears The One Ring or an artifact, each [GONDOR] knight is strength +1.
 */
public class Card9_033 extends AbstractCompanion {
    public Card9_033() {
        super(3, 7, 3, Culture.GONDOR, Race.MAN, null, "Isildur", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public int getResistance() {
        return 6;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Culture.GONDOR, Keyword.KNIGHT), new SpotCondition(self, Filters.hasAttached(Filters.or(CardType.THE_ONE_RING, CardType.ARTIFACT))), 1));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
                && Filters.and(Filters.inSkirmish, Keyword.RING_BEARER).accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new AddBurdenEffect(self, 2));
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, self.getOwner(), 3, 3, CardType.COMPANION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert 3 companions";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }

}
