package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Possession • Mount
 * Strength: +2
 * Game Text: Bearer must be a Nazgul. While at a plains site, bearer is strength +2. Discard this possession when at
 * an underground site.
 */
public class Card1_208 extends AbstractAttachable {
    public Card1_208() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.WRAITH, Keyword.MOUNT, "Black Steed");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.keyword(Keyword.NAZGUL);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "Strength +2, While at a Plains site, bearer is Strength +2", Filters.attachedTo(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                int bonus = (modifiersQuerying.hasKeyword(gameState, gameState.getCurrentSite(), Keyword.PLAINS)) ? 4 : 2;
                return result + bonus;
            }
        };
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getModifiersQuerying().hasKeyword(game.getGameState(), game.getGameState().getCurrentSite(), Keyword.UNDERGROUND)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Discard this possession");
            action.addEffect(new DiscardCardFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
