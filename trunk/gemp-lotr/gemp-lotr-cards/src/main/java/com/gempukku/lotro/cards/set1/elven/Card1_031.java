package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.*;

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
        super(2, Culture.ELVEN, "Asfaloth", true);
        addKeyword(Keyword.MOUNT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.keyword(Keyword.ELF), Filters.not(Filters.hasAttached(Filters.keyword(Keyword.MOUNT))));

        Map<Filter, Integer> costModifiers = new HashMap<Filter, Integer>();
        costModifiers.put(Filters.name("Arwen"), -2);

        appendAttachCardAction(actions, game, self, validTargetFilter, costModifiers);
        appendTransferPossessionAction(actions, game, self, validTargetFilter);

        return actions;
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
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO) {
            boolean isUnderground = game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.UNDERGROUND);
            if (isUnderground) {
                CostToEffectAction action = new CostToEffectAction(self, null, "Discard when Underground");
                action.addEffect(new DiscardCardFromPlayEffect(self));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
