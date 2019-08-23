package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardsFromHandOnTopOfDrawDeckEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * •Narya, Elven Ring of Power
 * Artifact • Ring
 * 1
 * Bearer must be Cirdan or Gandalf.
 * Skirmish: Stack 3 cards from hand on top of your draw deck to make a minion skirmishing an unbound companion strength -3.
 * The Shadow player may stack 3 cards on top of his or her draw deck to prevent this.
 * http://www.lotrtcg.org/coreset/elven/naryaerop(r2).jpg
 */
public class Card20_097 extends AbstractAttachableFPPossession {
    public Card20_097() {
        super(0, 0, 1, Culture.ELVEN, PossessionClass.RING, "Narya", "Elven Ring of Power", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.or(Filters.name("Cirdan"), Filters.gandalf);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.hasCardInHand(game, playerId, 3, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPutCardsFromHandOnTopOfDrawDeckEffect(action, playerId, 3, 3, Filters.any));
            action.appendEffect(
                    new PreventableEffect(action,
                            new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -3, CardType.MINION, Filters.inSkirmishAgainst(Filters.unboundCompanion)) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Make a minion skirmishing an unbound companion strength -3";
                                }
                            },
                            GameUtils.getShadowPlayers(game),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new ChooseAndPutCardsFromHandOnTopOfDrawDeckEffect(action, playerId, 3, 3, Filters.any) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Stack 3 cards from hand on top of your draw deck";
                                        }
                                    };
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
