package com.gempukku.lotro.cards.set32.smaug;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ForEachBurdenYouSpotEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Smaug
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: You may exert Smaug twice to play this event from your discard pile. Spot X doubts to make
 * a [DWARVEN] companion strength -X.
 */
public class Card32_058 extends AbstractEvent {
    public Card32_058() {
        super(Side.SHADOW, 2, Culture.GUNDABAD, "Dissension", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ForEachBurdenYouSpotEffect(playerId) {
                    @Override
                    public void burdensSpotted(int burdensSpotted) {
                        action.appendEffect(
                                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -burdensSpotted, Filters.and(Culture.DWARVEN, CardType.COMPANION)));
                    }
                });
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.canPlayFromDiscard(playerId, game, self)
                && PlayConditions.canExert(self, game, 2, Filters.name("Smaug"))) {
            final CostToEffectAction action = PlayUtils.getPlayCardAction(game, self, 0, Filters.any, false);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.name("Smaug")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
