package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.IncrementTurnLimitEffect;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: Eastemnet Plains
 * Set: Second Edition
 * Side: None
 * Site Number: 4
 * Shadow Number: 4
 * Card Number: 1U287
 * Game Text: Plains. Fellowship or Regroup: Make the move limit +1 for each culture over 2 you can spot in the fellowship
 * (limit once per turn.)
 */
public class Card40_287 extends AbstractSite {
    public Card40_287() {
        super("Eastemnet Plains", Block.SECOND_ED, 4, 4, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (GameUtils.isFP(game, playerId)
                && PlayConditions.checkTurnLimit(game, self, 1)
                && (PlayConditions.canUseSiteDuringPhase(game, Phase.FELLOWSHIP, self)
                || PlayConditions.canUseSiteDuringPhase(game, Phase.REGROUP, self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new IncrementTurnLimitEffect(self, 1));
            int countCultures = GameUtils.getSpottableCulturesCount(game.getGameState(), game.getModifiersQuerying(),
                    CardType.COMPANION);
            if (countCultures > 2)
                action.appendEffect(
                        new AddUntilEndOfTurnModifierEffect(
                                new MoveLimitModifier(self, countCultures - 2)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
