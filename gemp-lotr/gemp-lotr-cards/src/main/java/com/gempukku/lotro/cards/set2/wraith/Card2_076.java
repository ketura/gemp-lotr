package com.gempukku.lotro.cards.set2.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
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
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot Frodo and a Nazgul. Plays on Sam. Sam's game text does not apply.
 */
public class Card2_076 extends AbstractAttachable {
    public Card2_076() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.WRAITH, null, "Helpless");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Frodo"))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL));
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Sam");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "Sam's game text does not apply", Filters.name("Sam"), new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
                    @Override
                    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                        PhysicalCard actionSource = action.getActionSource();
                        if (actionSource != null
                                && actionSource.getBlueprint().getName().equals("Sam"))
                            return false;
                        return true;
                    }
                });
    }
}
