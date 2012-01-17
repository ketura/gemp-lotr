package com.gempukku.lotro.cards.set15.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: Each time an Elf wins a skirmish, you may exert that Elf or Aragorn to place an [ELVEN] token here.
 * At the start of each maneuver phase, you may remove an [ELVEN] token here to draw a card.
 */
public class Card15_016 extends AbstractPermanent {
    public Card15_016() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.ELVEN, Zone.SUPPORT, "Gift of the Evenstar", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Race.ELF)
                && PlayConditions.canExert(self, game, Filters.or(Filters.aragorn, ((CharacterWonSkirmishResult) effectResult).getWinner()))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.or(Filters.aragorn, ((CharacterWonSkirmishResult) effectResult).getWinner())));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ELVEN));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && PlayConditions.canRemoveTokens(game, self, Token.ELVEN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.ELVEN));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
