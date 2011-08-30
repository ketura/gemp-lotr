package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. The twilight cost of your [ISENGARD] events is -1. Skirmish: Discard this
 * condition to make an Uruk-hai strength +2.
 */
public class Card1_133 extends AbstractLotroCardBlueprint {
    public Card1_133() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Saruman's Ambition", "1_133");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
            return Collections.singletonList(action);
        }

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI))) {
            final CostToEffectAction action = new CostToEffectAction(self, "Discard this condition to make an Uruk-hai strength +2.");
            action.addCost(new DiscardCardFromPlayEffect(self));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.keyword(Keyword.URUK_HAI)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard urukHai) {
                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(urukHai), 2), Phase.SKIRMISH));
                        }
                    });

            return Collections.singletonList(action);
        }

        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.and(Filters.culture(Culture.ISENGARD), Filters.type(CardType.EVENT), Filters.owner(self.getOwner())), -1);
    }
}
