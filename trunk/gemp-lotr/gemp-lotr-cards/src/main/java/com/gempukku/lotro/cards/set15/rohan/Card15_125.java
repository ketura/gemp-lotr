package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Valiant. Response: If a hunter companion or valiant Man is about to take a wound, exert Eowyn
 * to prevent that wound.
 */
public class Card15_125 extends AbstractCompanion {
    public Card15_125() {
        super(2, 6, 3, 6, Culture.ROHAN, Race.MAN, null, Names.eowyn, "Willing Fighter", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        final Filter filter = Filters.or(Filters.and(CardType.COMPANION, Keyword.HUNTER), Filters.and(Race.MAN, Keyword.VALIANT));
        if (TriggerConditions.isGettingWounded(effect, game, filter)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose character to prevent wound to", filter));
            return Collections.singletonList(action);
        }
        return null;
    }
}
