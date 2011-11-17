package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Twilight Cost: 6
 * Type: Site
 * Site: 5K
 * Game Text: Plains. At the start of the maneuver phase, the Free Peoples player must discard his or her hand or
 * add 2 burdens.
 */
public class Card7_345 extends AbstractSite {
    public Card7_345() {
        super("Pelennor Flat", Block.KING, 5, 6, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            String fpPlayerId = game.getGameState().getCurrentPlayerId();
            possibleEffects.add(
                    new DiscardCardsFromHandEffect(self, fpPlayerId, new HashSet<PhysicalCard>(game.getGameState().getHand(fpPlayerId)), true) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard your hand";
                        }
                    });
            possibleEffects.add(
                    new AddBurdenEffect(self, 2) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Add 2 burdens";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, fpPlayerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
