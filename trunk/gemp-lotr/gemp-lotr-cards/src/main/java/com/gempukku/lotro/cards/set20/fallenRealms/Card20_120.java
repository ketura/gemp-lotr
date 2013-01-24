package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * Easterling Soldier
 * Fallen Realms	Minion • Man
 * 6	1	4
 * Easterling. Toil 1.
 * When you play this minion, you may place an Easterling from your discard pile at the bottom of your draw deck.
 */
public class Card20_120 extends AbstractMinion {
    public Card20_120() {
        super(3, 6, 1, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Soldier");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult,  self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardOnBottomOfDeckEffect(
                            action, playerId, 1, 1, Keyword.EASTERLING));
            return Collections.singletonList(action);
        }
        return null;
    }
}
