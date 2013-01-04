package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignAgainstResult;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 2
 * Site: 5
 * Game Text: Each time this minion is assigned to skirmish a character who has resistance 3 or less, you may exert this
 * minion to discard a possession borne by that character.
 */
public class Card11_200 extends AbstractMinion {
    public Card11_200() {
        super(3, 10, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Ruthless Uruk");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, null, Filters.and(Filters.character, Filters.maxResistance(3)), self)
                && PlayConditions.canSelfExert(self, game)) {
            AssignAgainstResult assignmentResult = (AssignAgainstResult) effectResult;
            final Set<PhysicalCard> assignedAgainst = assignmentResult.getAgainst();

            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Filters.attachedTo(Filters.in(assignedAgainst))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
