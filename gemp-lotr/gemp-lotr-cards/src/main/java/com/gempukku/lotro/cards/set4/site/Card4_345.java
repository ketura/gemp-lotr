package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 3
 * Type: Site
 * Site: 4T
 * Game Text: Mountain. When the fellowship moves to White Mountains, Theoden or 2 companions must exert.
 */
public class Card4_345 extends AbstractSite {
    public Card4_345() {
        super("White Mountains", Block.TWO_TOWERS, 4, 3, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
    }


    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && game.getGameState().getCurrentSite() == self) {
            String fpPlayerId = game.getGameState().getCurrentPlayerId();
            RequiredTriggerAction action = new RequiredTriggerAction(self);

            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, fpPlayerId, 1, 1, Filters.name("Theoden")) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Theoden";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, fpPlayerId, 2, 2, CardType.COMPANION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert 2 companions";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, fpPlayerId, possibleEffects));
            return Collections.singletonList(action);
        }

        return null;
    }

}
