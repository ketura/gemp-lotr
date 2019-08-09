package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * Coordinated Strike
 * Condition • Support Area
 * The twilight cost of this condition is -1 for each unwounded [Elven] archer companion you can spot.
 * Archery: Exert two [Elven] archer companions and discard this condition to make the fellowship archery total +2.
 * http://lotrtcg.org/coreset/elven/coordinatedstrike(r1).png
 */
public class Card20_078 extends AbstractPermanent {
    public Card20_078() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.ELVEN, "Coordinated Strike");
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -Filters.countActive(game, Culture.ELVEN, CardType.COMPANION, Keyword.ARCHER, Filters.unwounded);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canExert(self,game, 1, 2, Culture.ELVEN, CardType.COMPANION, Keyword.ARCHER)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, 1, Culture.ELVEN, CardType.COMPANION, Keyword.ARCHER));
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 2)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
