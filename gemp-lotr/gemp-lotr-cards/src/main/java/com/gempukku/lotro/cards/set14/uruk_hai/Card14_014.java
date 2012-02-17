package com.gempukku.lotro.cards.set14.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Expanded Middle-earth
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. At the start of each assignment phase, you may heal an Uruk-hai for each companion
 * you can spot.
 */
public class Card14_014 extends AbstractMinion {
    public Card14_014() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk-hai Healer");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.ASSIGNMENT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int countCompanions = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
            for (int i = 0; i < countCompanions; i++)
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI));
            return Collections.singletonList(action);
        }
        return null;
    }
}
