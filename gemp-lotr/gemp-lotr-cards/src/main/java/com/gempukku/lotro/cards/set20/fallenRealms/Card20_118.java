package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;

import java.util.Collections;
import java.util.List;

/**
 * ❺ Easterling Regiment [Fal]
 * Minion • Man
 * Strength: 9   Vitality: 3   Roaming: 4
 * Easterling. Enduring. (For each wound on this character, this character is strength +2.)
 * Each time you exert this minion using toil, you may add a threat.
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/easterlingregiment(r3).jpg
 */
public class Card20_118 extends AbstractMinion {
    public Card20_118() {
        super(5, 9, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Regiment");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachExerted(game, effectResult, self) && ((ExertResult) effectResult).isForToil()) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
