package com.gempukku.lotro.cards.set19.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.modifiers.MayNotBearModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion • Creature
 * Strength: 4
 * Vitality: 2
 * Site: 4
 * Game Text: Tentacle. This minion cannot bear possessions. Discard this minion from play if not at a marsh.
 * This minion is strength +1 for each tentacle you spot.
 */
public class Card19_019 extends AbstractMinion {
    public Card19_019() {
        super(2, 4, 2, 4, Race.CREATURE, Culture.MORIA, "Reaching Tentacle");
        addKeyword(Keyword.TENTACLE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new MayNotBearModifier(self, self, CardType.POSSESSION));
        modifiers.add(
                new StrengthModifier(self, self, null, new CountActiveEvaluator(Keyword.TENTACLE)));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.location(game, Filters.not(Keyword.MARSH))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }


}
