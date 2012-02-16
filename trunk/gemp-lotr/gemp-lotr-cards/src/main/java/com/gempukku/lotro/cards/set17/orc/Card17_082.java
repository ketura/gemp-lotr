package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 1
 * Site: 4
 * Game Text: Each mounted [ORC] Orc is strength +1. Shadow: discard an [ORC] card from your hand to play an [ORC] mount
 * from your discard pile.
 */
public class Card17_082 extends AbstractMinion {
    public Card17_082() {
        super(2, 6, 1, 4, Race.ORC, Culture.ORC, "Orkish Rider");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Culture.ORC, Race.ORC, Filters.mounted), 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.ORC)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ORC, PossessionClass.MOUNT)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.ORC));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ORC, PossessionClass.MOUNT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
