package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAssignCharacterToMinionEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 6
 * Type: Minion • Troll
 * Strength: 13
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Fierce. To play, spot an [ORC] minion. Assignment: Exert this minion to assign it to
 * a companion bearing an artifact. The Free Peoples player may discard an artifact from play to prevent this.
 */
public class Card11_108 extends AbstractMinion {
    public Card11_108() {
        super(6, 13, 3, 5, Race.TROLL, Culture.ORC, "Beastly Olog-hai");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new PreventableEffect(action,
                            new ChooseAndAssignCharacterToMinionEffect(action, playerId, self, CardType.COMPANION, Filters.hasAttached(CardType.ARTIFACT)) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Assign the minion to a companion bearing an artifact";
                                }
                            }, game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new ChooseAndDiscardCardsFromPlayEffect(subAction, playerId, 1, 1, CardType.ARTIFACT) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Discard an artifact from play";
                                        }
                                    };
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
