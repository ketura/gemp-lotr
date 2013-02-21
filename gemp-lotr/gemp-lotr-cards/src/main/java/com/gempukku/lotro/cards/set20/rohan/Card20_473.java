package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * 1
 * Might of the Mark
 * Rohan	Event • Skirmish
 * Spot a [Rohan] fortification to make an unbound companion strength +2 (or strength +3 and damage +1 if you can
 * spot 3 [Rohan] fortifications).
 */
public class Card20_473 extends AbstractEvent {
    public Card20_473() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Might of the Mark", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ROHAN, Keyword.FORTIFICATION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ConditionEvaluator(2, 3, new SpotCondition(3, Culture.ROHAN, Keyword.FORTIFICATION)), Filters.unboundCompanion) {
                    @Override
                    protected void selectedCharacterCallback(PhysicalCard selectedCharacter) {
                        if (PlayConditions.canSpot(game, 3, Culture.ROHAN, Keyword.FORTIFICATION))
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, selectedCharacter, Keyword.DAMAGE, 1)));
                    }
                });
        return action;
    }
}
