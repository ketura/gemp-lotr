package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. When you play this minion at a battleground site, spot an unbound companion to make that
 * companion resistance -2 until the end of the turn.
 */
public class Card11_189 extends AbstractMinion {
    public Card11_189() {
        super(4, 10, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Intimidating Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.location(game, Keyword.BATTLEGROUND)
                && PlayConditions.canSpot(game, Filters.unboundCompanion)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, self.getOwner(), "Choose an unbound companion", Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfTurnModifierEffect(
                                            new ResistanceModifier(self, card, -2)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
