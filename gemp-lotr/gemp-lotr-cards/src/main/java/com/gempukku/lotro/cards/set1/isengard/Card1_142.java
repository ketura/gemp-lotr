package com.gempukku.lotro.cards.set1.isengard;

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

import java.util.Collections;
import java.util.List;

/**
 * et: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Search. To play, spot an Uruk-hai. Plays to your support area. While the Ring-bearer is exhausted or you
 * can spot 5 burdens, the move limit for this turn is -1 (to a minimum of 1).
 */
public class Card1_142 extends AbstractLotroCardBlueprint {
    public Card1_142() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Traitor's Voice");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI));
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self));
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "While the Ring-bearer is exhausted or you can spot 5 burdens, the move limit for this turn is -1 (to a minimum of 1)", null, new ModifierEffect[]{ModifierEffect.MOVE_LIMIT_MODIFIER}) {
            @Override
            public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
                String currentPlayerId = gameState.getCurrentPlayerId();
                if (gameState.getBurdens(currentPlayerId) >= 5
                        || !PlayConditions.canExert(gameState, modifiersQuerying, gameState.getRingBearer(currentPlayerId)))
                    return result - 1;
                return result;
            }
        };
    }
}
