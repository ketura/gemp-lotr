package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
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
 * Set: The Short Rest
 * Twilight Cost: 3
 * Type: Site
 * Site: 4
 * Game Text: Mountain. Forest. When the fellowship moves to Wooded Steep Cliff, Thorin or 2 other companions must exert.
 */
public class Card31_049 extends AbstractSite {
    public Card31_049() {
        super("Wooded Steep Cliff", SitesBlock.HOBBIT, 4, 3, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
		addKeyword(Keyword.FOREST);
    }


    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            String fpPlayerId = game.getGameState().getCurrentPlayerId();
            RequiredTriggerAction action = new RequiredTriggerAction(self);

            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, fpPlayerId, 1, 1, Filters.name("Thorin")) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert Thorin";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, fpPlayerId, 2, 2, CardType.COMPANION, Filters.not(Filters.name("Thorin"))) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert 2 other companions";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, fpPlayerId, possibleEffects));
            return Collections.singletonList(action);
        }

        return null;
    }

}