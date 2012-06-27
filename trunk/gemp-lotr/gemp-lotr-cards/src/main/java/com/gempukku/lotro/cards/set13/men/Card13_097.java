package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 12
 * Vitality: 2
 * Site: 4
 * Game Text: To play, spot a [MEN] card in your support area. Shadow: Exert this minion and spot a Free Peoples
 * condition to reinforce a [MEN] token for each card you spot that has the same card title as that condition.
 */
public class Card13_097 extends AbstractMinion {
    public Card13_097() {
        super(4, 12, 2, 4, Race.MAN, Culture.MEN, "Pirate Cutthroat");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.MEN, Filters.owner(playerId), Zone.SUPPORT);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canSpot(game, Side.FREE_PEOPLE, CardType.CONDITION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Free Peoples condition", Side.FREE_PEOPLE, CardType.CONDITION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard condition) {
                            action.appendEffect(
                                    new ForEachYouSpotEffect(playerId, Filters.name(condition.getBlueprint().getName())) {
                                        @Override
                                        protected void spottedCards(int spotCount) {
                                            for (int i = 0; i < spotCount; i++)
                                                action.appendEffect(
                                                        new ReinforceTokenEffect(self, playerId, Token.MEN));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
