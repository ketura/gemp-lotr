package com.gempukku.lotro.cards.set32.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Twilight Cost: 8
 * Type: Site
 * Site: 5
 * Game Text: Forest. At the start of your Shadow phase, you may discard Gandalf.
 * If you do, Wise allies participate in skirmishes until the regroup phase.
 */
public class Card32_050 extends AbstractSite {
    public Card32_050() {
        super("Dol Guldur", SitesBlock.HOBBIT, 5, 8, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
         if (TriggerConditions.startOfPhase(game, effectResult, Phase.SHADOW)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.gandalf)
                && !playerId.equals(game.getGameState().getCurrentPlayerId())) {
                
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.gandalf));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new AllyParticipatesInArcheryFireAndSkirmishesModifier(self,
                                    Filters.and(Keyword.WISE, CardType.ALLY)), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
