package com.gempukku.lotro.cards.set1.sauron;

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
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot a [SAURON] Orc. Plays on a companion (except an Elf). While at a sanctuary, bearer cannot
 * heal.
 */
public class Card1_283 extends AbstractAttachable {
    public Card1_283() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.SAURON, null, "You Bring Great Evil");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.ELF)));
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC));
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "While at a sanctuary, bearer cannot heal.", Filters.attachedTo(self), new ModifierEffect[]{ModifierEffect.WOUND_MODIFIER}) {
            @Override
            public boolean canBeHealed(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result) {
                if (modifiersQuerying.hasKeyword(gameState, gameState.getCurrentSite(), Keyword.SANCTUARY))
                    return false;
                return result;
            }
        };
    }
}
