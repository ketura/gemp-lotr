package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.CancelActivatedEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Orthanc Retainer
 * Isengard	Minion • Orc
 * 5	3	4
 * Lurker.
 * Response: If a special ability is used in a skirmish involving an Uruk-hai, exert this minion to prevent that special ability.
 */
public class Card20_212 extends AbstractMinion {
    public Card20_212() {
        super(2, 5, 3, 4, Race.ORC, Culture.ISENGARD, "Orthanc Retainer");
        addKeyword(Keyword.LURKER);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.activated(game, effectResult, Filters.any)
                && PlayConditions.isActive(game, Race.URUK_HAI, Filters.inSkirmish)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new CancelActivatedEffect(self, (ActivateCardResult) effectResult));
            return Collections.singletonList(action);
        }
        return null;
    }
}
