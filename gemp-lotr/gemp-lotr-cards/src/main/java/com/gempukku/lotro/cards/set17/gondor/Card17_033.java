package com.gempukku.lotro.cards.set17.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 5
 * Game Text: Ranger. Hunter 1. When this is in your starting fellowship and you can spot an unbound [GONDOR] companion
 * with cost 3 or more, his twilight cost is -2. Maneuver: Exert this companion and spot a roaming minion to make him
 * defender +1 until the start of the regroup phase.
 */
public class Card17_033 extends AbstractCompanion {
    public Card17_033() {
        super(3, 6, 3, 5, Culture.GONDOR, Race.MAN, null, "Ranger of the White Tree");
        addKeyword(Keyword.RANGER);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        if (game.getGameState().getCurrentPhase() == Phase.PLAY_STARTING_FELLOWSHIP
                && Filters.canSpot(game, Culture.GONDOR, Filters.unboundCompanion,
                new Filter() {
                    @Override
                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                        return physicalCard.getBlueprint().getTwilightCost() >= 3;
                    }
                })) {
            return -2;
        }
        return 0;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canSpot(game, CardType.MINION, Keyword.ROAMING)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, self, Keyword.DEFENDER, 1), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
