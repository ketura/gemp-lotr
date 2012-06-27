package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: Maneuver: Exert this minion to play a [MEN] minion (or 2 if you spot 6 companions) from a [MEN] possession
 * as if from hand.
 */
public class Card17_055 extends AbstractMinion {
    public Card17_055() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Sunland Guard");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canPlayFromStacked(playerId, game, Filters.and(Culture.MEN, CardType.POSSESSION), Culture.MEN, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, Filters.and(Culture.MEN, CardType.POSSESSION), Culture.MEN, CardType.MINION));
            if (PlayConditions.canSpot(game, 6, CardType.COMPANION))
                action.appendEffect(
                        new OptionalEffect(action, playerId,
                                new ChooseAndPlayCardFromStackedEffect(playerId, Filters.and(Culture.MEN, CardType.POSSESSION), Culture.MEN, CardType.MINION) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Play 2nd minion";
                                    }
                                }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
