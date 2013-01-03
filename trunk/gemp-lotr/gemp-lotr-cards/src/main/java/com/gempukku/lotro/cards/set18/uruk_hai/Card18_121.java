package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Each [URUK-HAI] minion bearing a hand weapon cannot take wounds during the archery phase.
 * Skirmish: Discard this condition from play to make an [URUK-HAI] minion bearing a possession strength +2.
 */
public class Card18_121 extends AbstractPermanent {
    public Card18_121() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Pikes Upon Pikes");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self,
                new PhaseCondition(Phase.ARCHERY),
                Filters.and(Culture.URUK_HAI, CardType.MINION, Filters.hasAttached(PossessionClass.HAND_WEAPON)));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.URUK_HAI, CardType.MINION, Filters.hasAttached(CardType.POSSESSION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
