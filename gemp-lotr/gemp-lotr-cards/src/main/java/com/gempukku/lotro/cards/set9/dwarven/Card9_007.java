package com.gempukku.lotro.cards.set9.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Strength: +1
 * Game Text: Bearer must be a Dwarf. Bearer is damage +2. Regroup: Spot a Dwarf that is damage +X to heal X Dwarves.
 * Discard this artifact.
 */
public class Card9_007 extends AbstractAttachableFPPossession {
    public Card9_007() {
        super(0, 1, 0, Culture.DWARVEN, CardType.ARTIFACT, PossessionClass.RING, "Ring of Fury", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 2));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf with damage +X", Race.DWARF, Keyword.DAMAGE) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int damageCount = game.getModifiersQuerying().getKeywordCount(game.getGameState(), card, Keyword.DAMAGE);
                            action.insertEffect(
                                    new ChooseAndHealCharactersEffect(action, playerId, damageCount, damageCount, Race.DWARF));
                        }
                    });
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
