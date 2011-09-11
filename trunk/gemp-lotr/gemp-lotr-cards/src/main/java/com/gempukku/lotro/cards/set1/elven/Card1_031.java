package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession � Mount
 * Strength: +2
 * Game Text: Bearer must be an Elf. When played on Arwen, Asfaloth's twilight cost is -2. While at a plains site,
 * bearer is strength +2. Discard Asfaloth when at an underground site.
 */
public class Card1_031 extends AbstractAttachableFPPossession {
    public Card1_031() {
        super(2, Culture.ELVEN, Keyword.MOUNT, "Asfaloth", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.ELF);
    }

    @Override
    protected Map<Filter, Integer> getAttachCostModifiers(String playerId, LotroGame game, PhysicalCard self) {
        Map<Filter, Integer> costModifiers = new HashMap<Filter, Integer>();
        costModifiers.put(Filters.name("Arwen"), -2);
        return costModifiers;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "Strength +2, if at Plains another +2", Filters.attachedTo(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                return result + (modifiersQuerying.hasKeyword(gameState, gameState.getCurrentSite(), Keyword.PLAINS) ? 4 : 2);
            }
        };
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.UNDERGROUND)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Discard when Underground");
            action.addEffect(new DiscardCardFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
