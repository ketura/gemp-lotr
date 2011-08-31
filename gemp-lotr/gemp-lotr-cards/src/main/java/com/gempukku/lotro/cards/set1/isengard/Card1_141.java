package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot Saruman or an Uruk-hai. Plays to your support area. Each archer companion and archer ally
 * is strength -1.
 */
public class Card1_141 extends AbstractLotroCardBlueprint {
    public Card1_141() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Their Arrows Enrage", "1_141");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.keyword(Keyword.URUK_HAI), Filters.name("Saruman")))) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.keyword(Keyword.ARCHER), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY))), -1);
    }
}
