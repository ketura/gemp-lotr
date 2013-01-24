package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * Easterling Conscript
 * Fallen Realms	Minion • Man
 * 6	1	4
 * Easterling. Toil 1.
 * When you play this minion, you may spot a [Fallen Realms] minion to discard a Free Peoples condition.
 */
public class Card20_113 extends AbstractMinion {
    public Card20_113() {
        super(3, 6, 1, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Conscript");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
            && PlayConditions.canSpot(game, Filters.not(self), Culture.FALLEN_REALMS, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
