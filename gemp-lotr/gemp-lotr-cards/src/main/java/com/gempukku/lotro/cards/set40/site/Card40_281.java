package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: Ettenmoors
 * Set: Second Edition
 * Side: None
 * Site Number: 2
 * Shadow Number: 3
 * Card Number: 1U281
 * Game Text: Plains. Skirmish: Exert a non-enduring character to make that character strength +2.
 */
public class Card40_281 extends AbstractSite {
    public Card40_281() {
        super("Ettenmoors", SitesBlock.SECOND_ED, 2, 3, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Filters.character, Filters.owner(playerId), Filters.not(Keyword.ENDURING))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.character, Filters.owner(playerId), Filters.not(Keyword.ENDURING)) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, character, 2)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
