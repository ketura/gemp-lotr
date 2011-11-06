package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 7
 * Type: Minion • Man
 * Strength: 14
 * Vitality: 4
 * Site: 4
 * Game Text: Southron. Archer. Ambush (1). To play, spot a Southron. Assignment: Spot 7 companions to assign this
 * minion to the Ringbearer. The Free Peoples player may make you discard a companion (except the Ring-bearer)
 * to prevent this.
 */
public class Card4_256 extends AbstractMinion {
    public Card4_256() {
        super(7, 14, 4, 4, Race.MAN, Culture.RAIDER, "Southron Troop");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.AMBUSH, 1);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, Filters.keyword(Keyword.SOUTHRON));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canSpot(game, 7, Filters.type(CardType.COMPANION))
                && PlayConditions.canCardAssignToSkirmish(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new PreventableEffect(action,
                            new AssignmentEffect(playerId, Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER)), self),
                            game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String fpPlayerId) {
                                    return new ChooseAndDiscardCardsFromPlayEffect(subAction, playerId, 1, 1, Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER))) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Make " + playerId + " discard a companion (except the Ring-bearer)";
                                        }
                                    };
                                }
                            }
                    ));
            return Collections.singletonList(action);
        }
        return null;
    }
}
