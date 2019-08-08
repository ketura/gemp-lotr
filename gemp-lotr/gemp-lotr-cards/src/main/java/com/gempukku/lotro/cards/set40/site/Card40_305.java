package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
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
 * Title: Pelennor Battlefield
 * Set: Second Edition
 * Side: None
 * Site Number: 8
 * Shadow Number: 8
 * Card Number: 1U305
 * Game Text: Battleground. Plains.
 * Regroup: The Free Peoples player may place an unbound companion in the dead pile to make the move limit +1.
 */
public class Card40_305 extends AbstractSite {
    public Card40_305() {
        super("Pelennor Battlefield", Block.SECOND_ED, 8, 8, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.REGROUP, self)
                && GameUtils.isFP(game, playerId)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a companion to kill") {
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
