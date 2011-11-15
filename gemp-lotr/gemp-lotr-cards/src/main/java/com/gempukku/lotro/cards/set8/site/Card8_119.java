package com.gempukku.lotro.cards.set8.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Twilight Cost: 7
 * Type: Site
 * Site: 5K
 * Game Text: At the start of the regroup phase, the Free Peoples player must add 3 threats or choose an opponent who
 * may take control of a site.
 */
public class Card8_119 extends AbstractSite {
    public Card8_119() {
        super("Crashed Gate", Block.KING, 5, 7, Direction.LEFT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.startOfPhase(game, effectResult, Phase.REGROUP)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new AddThreatsEffect(game.getGameState().getCurrentPlayerId(), self, 3));
            possibleEffects.add(
                    new ChooseOpponentEffect(game.getGameState().getCurrentPlayerId()) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Choose an opponent who may take control of a site";
                        }

                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new OptionalEffect(action, opponentId,
                                            new TakeControlOfASiteEffect(self, opponentId)));
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
