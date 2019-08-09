package com.gempukku.lotro.cards.set12.uruk_hai;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDoAssignmentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: While you can spot 6 companions, each [URUK-HAI] minion is damage +1. Assignment: Discard this condition
 * to assign an [URUK-HAI] minion to a companion who has resistance 2 or less.
 */
public class Card12_145 extends AbstractPermanent {
    public Card12_145() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.URUK_HAI, "Shingle in a Storm");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Filters.and(Culture.URUK_HAI, CardType.MINION), new SpotCondition(6, CardType.COMPANION), Keyword.DAMAGE, 1));
}

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndDoAssignmentEffect(action, playerId, Culture.URUK_HAI, Filters.and(CardType.COMPANION, Filters.maxResistance(2))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
