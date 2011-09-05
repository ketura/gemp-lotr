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
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Their Arrows Enrage");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.keyword(Keyword.URUK_HAI), Filters.name("Saruman")));
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.keyword(Keyword.ARCHER), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY))), -1);
    }
}
