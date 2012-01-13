package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Discard this condition to make an [URUK-HAI] minion strength +1 for each of the following that
 * is true: it is at a battleground site; it is skirmishing a companion who has resistance 4 or less; it is bearing
 * a possession; you can spot an [URUK-HAI] minion not assigned to a skirmish.
 */
public class Card13_161 extends AbstractPermanent {
    public Card13_161() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Endless Assault");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an URUK-HAI minion", Culture.URUK_HAI, CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard minion) {
                            int bonus = 0;
                            if (PlayConditions.location(game, Keyword.BATTLEGROUND))
                                bonus++;
                            if (PlayConditions.canSpot(game, minion, Filters.inSkirmishAgainst(CardType.COMPANION, Filters.maxResistance(4))))
                                bonus++;
                            if (PlayConditions.canSpot(game, minion, Filters.hasAttached(CardType.POSSESSION)))
                                bonus++;
                            if (PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION, Filters.notAssignedToSkirmish))
                                bonus++;
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, minion, bonus), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
