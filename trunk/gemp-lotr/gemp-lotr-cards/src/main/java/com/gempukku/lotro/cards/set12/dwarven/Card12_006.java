package com.gempukku.lotro.cards.set12.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: Toil 2. (For each [DWARVEN] character you exert when playing this, its twilight cost is -2)
 * Skirmish: Discard this condition to make a Dwarf strength +3 (or +4 if he has resistance 4 or more).
 */
public class Card12_006 extends AbstractPermanent {
    public Card12_006() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Dwarven Skill");
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, new CardMatchesEvaluator(3, 4, Filters.minResistance(4)), Race.DWARF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
