package com.gempukku.lotro.cards.set17.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: To play, spot 2 minions. While you can spot Sauron, each other minion is damage +1. While the fellowship
 * is in the same region as Mount Doom, each companion is resistance -2.
 */
public class Card17_105 extends AbstractPermanent {
    public Card17_105() {
        super(Side.SHADOW, 2, CardType.ARTIFACT, Culture.SAURON, Zone.SUPPORT, "Throne of the Dark Lord", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(CardType.MINION, Filters.not(Filters.name("Sauron"))),
                        new SpotCondition(Filters.name("Sauron")), Keyword.DAMAGE, 1));
        modifiers.add(
                new ResistanceModifier(self, CardType.COMPANION,
                        new LocationCondition(
                                new Filter() {
                                    @Override
                                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                        PhysicalCard mountDoom = Filters.findFirstActive(gameState, modifiersQuerying, Filters.name("Mount Doom"));
                                        return mountDoom != null && (1 + ((mountDoom.getSiteNumber() - 1) / 3)) == GameUtils.getRegion(gameState);
                                    }
                                }), -2));
        return modifiers;
    }
}
