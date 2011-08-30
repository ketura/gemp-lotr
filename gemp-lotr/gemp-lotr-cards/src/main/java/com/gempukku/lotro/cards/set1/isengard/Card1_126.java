package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Maneuver: Make an Uruk-hai fierce until the regroup phase.
 */
public class Card1_126 extends AbstractLotroCardBlueprint {
    public Card1_126() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Hunt Them Down!", "1_126");
        addKeyword(Keyword.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.MANEUVER, self)) {
            final CostToEffectAction action = new CostToEffectAction(self, "Make an Uruk-hai fierce until the regroup phase.");
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.keyword(Keyword.URUK_HAI)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard urukHai) {
                            action.addEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(urukHai), Keyword.FIERCE), Phase.REGROUP));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
