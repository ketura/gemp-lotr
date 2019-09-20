package com.gempukku.lotro.cards.set32.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.ShuffleDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Vitality: +1
 * Game Text: Bearer must be Galadriel. At the start of each of your turns, you may heal a Wise ally.
 * Fellowship: Exert Galadriel and add 2 to take a Gandalf spell from your draw deck into your hand.
 */
public class Card32_014 extends AbstractAttachableFPPossession {
    public Card32_014() {
        super(0, 0, 1, Culture.ELVEN, CardType.ARTIFACT, PossessionClass.RING, "Nenya", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.galadriel;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            final Set<Culture> cultureTokens = new HashSet<Culture>();
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.ALLY, Keyword.WISE));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Filters.galadriel)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.galadriel));
            action.appendCost(
                    new AddTwilightEffect(self, 2));
            action.appendEffect(
                    new ChooseAndPutCardFromDeckIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF, Keyword.SPELL));
            action.appendEffect(
                    new ShuffleDeckEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
