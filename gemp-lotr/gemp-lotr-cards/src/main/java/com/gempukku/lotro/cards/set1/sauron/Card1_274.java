package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
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
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, exert [SAURON] a Orc. Plays to your support area. While there are 3 or more companions in
 * the dead pile, the move limit for this turn is -1 (to a minimum of 1).
 */
public class Card1_274 extends AbstractPermanent {
    public Card1_274() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SHADOW_SUPPORT, "Sauron's Defenses");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC), Filters.canExert());
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose a SAURON Orc", true, Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC), Filters.canExert()));
        return action;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "While there are 3 or more companions in the dead pile, the move limit for this turn is -1 (to a minimum of 1).", Filters.none(), new ModifierEffect[]{ModifierEffect.MOVE_LIMIT_MODIFIER}) {
            @Override
            public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
                if (Filters.filter(gameState.getDeadPile(gameState.getCurrentPlayerId()), gameState, modifiersQuerying, Filters.type(CardType.COMPANION)).size() >= 3)
                    return result - 1;
                return result;
            }
        };
    }
}
