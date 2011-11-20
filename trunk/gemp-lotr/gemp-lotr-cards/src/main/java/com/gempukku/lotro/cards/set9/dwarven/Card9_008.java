package com.gempukku.lotro.cards.set9.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
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
 * Game Text: Bearer must be a Dwarf. Bearer is damage +1. Maneuver: Take a [DWARVEN] event into hand from your discard
 * pile. Discard this artifact.
 */
public class Card9_008 extends AbstractAttachableFPPossession {
    public Card9_008() {
        super(0, 1, 0, Culture.DWARVEN, CardType.ARTIFACT, PossessionClass.RING, "Ring of Guile", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.EVENT));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
