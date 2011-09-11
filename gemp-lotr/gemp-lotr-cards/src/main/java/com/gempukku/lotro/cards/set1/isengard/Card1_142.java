package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * et: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Search. To play, spot an Uruk-hai. Plays to your support area. While the Ring-bearer is exhausted or you
 * can spot 5 burdens, the move limit for this turn is -1 (to a minimum of 1).
 */
public class Card1_142 extends AbstractPermanent {
    public Card1_142() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, Zone.SHADOW_SUPPORT, "Traitor's Voice");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI));
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "While the Ring-bearer is exhausted or you can spot 5 burdens, the move limit for this turn is -1 (to a minimum of 1)", null, new ModifierEffect[]{ModifierEffect.MOVE_LIMIT_MODIFIER}) {
            @Override
            public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
                String currentPlayerId = gameState.getCurrentPlayerId();
                if (gameState.getBurdens() >= 5
                        || !PlayConditions.canExert(gameState, modifiersQuerying, gameState.getRingBearer(currentPlayerId)))
                    return result - 1;
                return result;
            }
        };
    }
}
