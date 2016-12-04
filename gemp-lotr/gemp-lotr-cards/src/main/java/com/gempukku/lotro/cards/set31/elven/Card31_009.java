package com.gempukku.lotro.cards.set31.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally • Home 5 • Elf
 * Strength: 6
 * Vitality: 3
 * Site: 5
 * Game Text: Archer. Archery: Spot an Orc and exert Legolas to make the fellowship archery total +1
 * (or +2 if at a river or forest)
 */
public class Card31_009 extends AbstractAlly {
    public Card31_009() {
        super(2, Block.HOBBIT, 5, 6, 3, Race.ELF, Culture.ELVEN, "Legolas", "Prince of Mirkwood", true);
		addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canExert(self, game, self)
                && PlayConditions.canSpot(game, Race.ORC)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
							new ArcheryTotalModifier(self, Side.FREE_PEOPLE, null, new ConditionEvaluator(1, 2, new LocationCondition(Filters.or(Keyword.RIVER, Keyword.FOREST))))));
            return Collections.singletonList(action);
        }
        return null;
    }
}