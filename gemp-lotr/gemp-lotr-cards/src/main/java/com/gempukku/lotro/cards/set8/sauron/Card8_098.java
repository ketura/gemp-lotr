package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. When you play this minion, you may remove a burden to play a besieger stacked on a site
 * you control.
 */
public class Card8_098 extends AbstractMinion {
    public Card8_098() {
        super(3, 9, 2, 5, Race.ORC, Culture.SAURON, "Gorgoroth Looter");
        addKeyword(Keyword.BESIEGER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canRemoveBurdens(game, self, 1)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, Filters.siteControlled(playerId), Keyword.BESIEGER));
            return Collections.singletonList(action);
        }
        return null;
    }
}
