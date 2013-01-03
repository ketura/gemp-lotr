package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * Dunlending Plunderer
 * Dunland	Minion • Man
 * 9	1	3
 * When you play Dunlending Plunder, you may discard a [Dunland] card from hand. If you do, spot another [Dunland] man
 * to discard a Free Peoples possession.
 */
public class Card20_009 extends AbstractMinion {
    public Card20_009() {
        super(3, 9, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Plunderer");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.DUNLAND)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.DUNLAND, Race.MAN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.DUNLAND));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
