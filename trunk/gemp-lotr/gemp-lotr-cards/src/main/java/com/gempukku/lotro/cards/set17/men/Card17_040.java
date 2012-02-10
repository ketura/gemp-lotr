package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.conditions.MinThreatCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Possession • Mount
 * Strength: +3
 * Game Text: Bearer must be a [MEN] Man. While you can spot a threat, bearer is fierce. While you can spot 3 threats,
 * bearer is damage +1. While you can spot 5 threats, you may play this from your discard pile.
 */
public class Card17_040 extends AbstractAttachable {
    public Card17_040() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.MEN, PossessionClass.MOUNT, "Beast of War");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MEN, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 3));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new MinThreatCondition(1), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), new MinThreatCondition(3), Keyword.DAMAGE, 1));
        return modifiers;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canSpotThreat(game, 5)
                && PlayConditions.canPlayFromDiscard(playerId, game, self)) {
            return Collections.singletonList(
                    getPlayCardAction(playerId, game, self, 0, false));
        }
        return null;
    }
}
