package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Arwen, Bane of the Black Riders
 * Elven    Companion • Elf
 * 6	3	8
 * Ranger.
 * At the start of the regroup phase, you may exert Arwen to reveal the top card of your draw deck. If it is
 * an [Elven] card, you may wound a minion (or a Nazgul twice).
 */
public class Card20_073 extends AbstractCompanion {
    public Card20_073() {
        super(2, 6, 3, 8, Culture.ELVEN, Race.ELF, null, "Arwen", "Lady of Imladris", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ForEachYouSpotEffect(playerId, Keyword.RIVENDELL, CardType.ALLY) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            for (int i=0; i<spotCount; i++)
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
