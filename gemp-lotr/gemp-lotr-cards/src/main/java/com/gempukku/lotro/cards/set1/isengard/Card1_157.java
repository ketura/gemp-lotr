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
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot an Uruk-hai, the fellowship archery total is -1.
 */
public class Card1_157 extends AbstractLotroCardBlueprint {
    public Card1_157() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Uruk-hai Armory");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "While you can spot an Uruk-hai, the fellowship archery total is -1.", null, new ModifierEffect[]{ModifierEffect.ARCHERY_MODIFIER}) {
            @Override
            public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersLogic, Side side, int result) {
                if (side == Side.FREE_PEOPLE
                        && Filters.canSpot(gameState, modifiersLogic, Filters.keyword(Keyword.URUK_HAI)))
                    return result - 1;
                return result;
            }
        };
    }
}
