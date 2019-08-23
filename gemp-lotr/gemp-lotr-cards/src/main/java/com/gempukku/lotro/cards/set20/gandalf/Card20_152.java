package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * •Barliman Butterbur, Jovial Innkeep
 * Ally • Man • Bree
 * 1	2
 * To play, spot Gandalf.
 * Fellowship or Regroup: Exert Barliman Butterbur to heal a companion.
 * http://lotrtcg.org/coreset/gandalf/barlimanbutterburji(r1).png
 */
public class Card20_152 extends AbstractAlly {
    public Card20_152() {
        super(0, null, 0, 1, 2, Race.MAN, Culture.GANDALF, "Barliman Butterbur", "Jovial Innkeep", true);
        addKeyword(Keyword.BREE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self))
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
