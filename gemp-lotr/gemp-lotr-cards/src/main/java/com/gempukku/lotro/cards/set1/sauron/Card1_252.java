package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.CorruptRingBearerEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, exert a [SAURON] Orc. Plays to your support area. If you can spot 5 burdens and the Free Peoples
 * player has no cards in his or her draw deck, the Ring-bearer is corrupted.
 */
public class Card1_252 extends AbstractPermanent {
    public Card1_252() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.SAURON, "The Irresistible Shadow");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Culture.SAURON, Race.ORC));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getGameState().getBurdens() >= 5
                && game.getGameState().getDeck(game.getGameState().getCurrentPlayerId()).size() == 0) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new CorruptRingBearerEffect());
            return Collections.singletonList(action);
        }
        return null;
    }
}
