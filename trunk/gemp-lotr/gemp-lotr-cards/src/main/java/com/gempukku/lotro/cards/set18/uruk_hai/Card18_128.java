package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Response: If an [URUK-HAI] minion is about to take a wound, exert this minion
 * to prevent that wound.
 */
public class Card18_128 extends AbstractMinion {
    public Card18_128() {
        super(4, 8, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Mystic");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.URUK_HAI, CardType.MINION)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose an URUK_HAI minion", Culture.URUK_HAI, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
