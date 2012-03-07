package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 7
 * Game Text: Ranger. Hunter 2. If Faramir is in your starting fellowship, his twilight cost is -1.
 * Maneuver: Exert Faramir to heal another companion with resistance 5 or more.
 */
public class Card18_048 extends AbstractCompanion {
    public Card18_048() {
        super(3, 7, 3, 7, Culture.GONDOR, Race.MAN, null, "Faramir", true);
        addKeyword(Keyword.RANGER);
        addKeyword(Keyword.HUNTER, 2);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP)
            return -1;
        return 0;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Filters.not(self), CardType.COMPANION, Filters.minResistance(5)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
