package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Search. Plays to your support area. While you can spot 7 companions, the move limit for this turn is -1
 * (to a minimum of 1).
 */
public class Card1_260 extends AbstractLotroCardBlueprint {
    public Card1_260() {
        super(Side.SHADOW, CardType.CONDITION, Culture.SAURON, "The Number Must Be Few");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier);
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT, twilightModifier);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "While you can spot 7 companions, the move limit for this turn is -1 (to a minimum of 1).", Filters.none(), new ModifierEffect[]{ModifierEffect.MOVE_LIMIT_MODIFIER}) {
            @Override
            public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
                if (Filters.countActive(gameState, modifiersQuerying, Filters.type(CardType.COMPANION)) >= 7)
                    return result - 1;
                return result;
            }
        };
    }
}
