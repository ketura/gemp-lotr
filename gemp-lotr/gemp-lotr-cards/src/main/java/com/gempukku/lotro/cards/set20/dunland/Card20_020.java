package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * • Freca, Oathsworn Savage
 * Dunland	Minion • Man
 * 9	1	3
 * While you can spot Saruman, Freca's twilight cost is -2.
 * Maneuver or Regroup: Stack Freca on a site you control.
 * Shadow: If stacked on a site you control, spot Saruman or a [Dunland] Man to play Freca; his twilight cost is -2.
 */
public class Card20_020 extends AbstractMinion {
    public Card20_020() {
        super(4, 9, 1, 3, Race.MAN, Culture.DUNLAND, "Freca", "Oathsworn Savage", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, Filters.saruman))
            return -2;
        return 0;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if ((PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                || PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose site you control", Filters.siteControlled(playerId)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new StackCardFromPlayEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromStacked(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseStackedShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.stackedOn(self, game, Filters.siteControlled(self.getOwner()))
                && PlayConditions.canSpot(game, Filters.or(Filters.saruman, Filters.and(Filters.not(self), Culture.DUNLAND, Race.MAN)))
                && checkPlayRequirements(playerId, game, self, 0, -2, false, false)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, -2, false));
        }
        return null;
    }
}
