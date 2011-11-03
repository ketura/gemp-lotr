package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: Southron. Shadow: Remove (3) and spot 3 [RAIDER] Men to make one of those Men damage +1 until
 * the regroup phase.
 */
public class Card7_161 extends AbstractMinion {
    public Card7_161() {
        super(4, 9, 2, 4, Race.MAN, Culture.RAIDER, "Southron Brigand");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 3)
                && PlayConditions.canSpot(game, 3, Culture.RAIDER, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a RAIDER Man", Culture.RAIDER, Race.MAN) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            game.getModifiersEnvironment().addUntilStartOfPhaseModifier(
                                    new KeywordModifier(self, card, Keyword.DAMAGE, 1), Phase.REGROUP);
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
