package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.costs.DiscardCardsFromPlayCost;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.effects.PreventEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Possession • Bracers
 * Strength: +1
 * Game Text: Bearer must be a Dwarf. Response: If bearer is about to take a wound, discard this possession to prevent
 * that wound.
 */
public class Card2_003 extends AbstractAttachableFPPossession {
    public Card2_003() {
        super(1, Culture.DWARVEN, Keyword.BRACERS, "Dwarven Bracers");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.DWARF);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), 1);
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND
                && ((WoundCharacterEffect) effect).getCardsToBeAffected(game).contains(self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.RESPONSE);
            action.appendCost(
                    new DiscardCardsFromPlayCost(self));
            action.appendEffect(
                    new CardAffectsCardEffect(self, self.getAttachedTo()));
            action.appendEffect(
                    new PreventEffect((WoundCharacterEffect) effect, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
