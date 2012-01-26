package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.CancelKeywordBonusTargetModifier;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusTargetModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: To play, spot a [MEN] minion. Bearer must be a companion. While bearer is skirmishing a [MEN] minion,
 * bearer loses from possessions all strength and damage bonuses.
 */
public class Card11_087 extends AbstractAttachable {
    public Card11_087() {
        super(Side.SHADOW, CardType.CONDITION, 3, Culture.MEN, null, "Lathspell");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canSpot(game, Culture.MEN, CardType.MINION);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return CardType.COMPANION;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CancelStrengthBonusTargetModifier(self,
                        Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Culture.MEN, CardType.MINION)),
                        CardType.POSSESSION));
        modifiers.add(
                new CancelKeywordBonusTargetModifier(self, Keyword.DAMAGE,
                        Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Culture.MEN, CardType.MINION)),
                        CardType.POSSESSION));
        return modifiers;
    }
}
