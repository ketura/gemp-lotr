package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Pelennor Battlefield
 * 8	8
 * Battleground. Plains.
 * Regroup: The Free Peoples player may place an unbound companion in the dead pile to make the move limit +1 for this turn.
 */
public class Card20_463 extends AbstractSite {
    public Card20_463() {
        super("Pelennor Battlefield", SitesBlock.SECOND_ED, 8, 8, null);
        addKeyword(Keyword.BATTLEGROUND);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.REGROUP, self)
                && GameUtils.isFP(game, playerId)
                && PlayConditions.isActive(game, Filters.unboundCompanion)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion to place in the dead pile", Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendCost(
                                    new KillEffect(card, KillEffect.Cause.CARD_EFFECT));
                        }
                    });
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new MoveLimitModifier(self, 1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
