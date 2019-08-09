package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * ❸ Easterling Grunt [Fal]
 * Minion • Man
 * Strength: 8   Vitality: 2   Roaming: 4
 * Easterling.
 * Each time this minion exerts, you may make him fierce until the regroup phase
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/easterlinggrunt(r3).jpg
 */
public class Card20_480 extends AbstractMinion {
    public Card20_480() {
        super(3, 8, 2, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Grunt");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachExerted(game, effectResult, self)) {
            OptionalTriggerAction action =new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.FIERCE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
