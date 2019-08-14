package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * ❹ Southron Spearman [Fal]
 * Minion • Man
 * Strength: 9   Vitality: 2   Roaming: 4
 * Southron.
 * Regroup: Remove ❸ to make the Free Peoples player wound a companion.
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/southronspearman(r3).jpg
 */
public class Card20_142 extends AbstractMinion {
    public Card20_142() {
        super(4, 9, 2, 4, Race.MAN, Culture.FALLEN_REALMS, "Southron Spearman");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
