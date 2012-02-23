package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. When you play this minion, exert a companion for each site you control.
 */
public class Card17_128 extends AbstractMinion {
    public Card17_128() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Invader");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(self.getOwner()));
            for (int i = 0; i < count; i++)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
