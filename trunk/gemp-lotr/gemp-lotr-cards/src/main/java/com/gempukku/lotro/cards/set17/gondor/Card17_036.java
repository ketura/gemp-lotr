package com.gempukku.lotro.cards.set17.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: To play, spot Denethor (or 2 [GONDOR] companions). Each time a minion is killed or discarded from play
 * during a skirmish, you may reinforce a [GONDOR] token. Skirmish: Remove 3 [GONDOR] tokens to discard a possession.
 */
public class Card17_036 extends AbstractPermanent {
    public Card17_036() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.GONDOR, Zone.SUPPORT, "Throne of Minas Tirith", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                &&
                (PlayConditions.canSpot(game, Filters.name("Denethor"))
                        || PlayConditions.canSpot(game, 2, Culture.GONDOR, CardType.COMPANION));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && TriggerConditions.forEachDiscardedFromPlay(game, effectResult, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.setTriggerIdentifier(self.getCardId()+"-"+((DiscardCardsFromPlayResult) effectResult).getDiscardedCard());
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.GONDOR));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokensFromAnything(game, Token.GONDOR, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.GONDOR, 1, Filters.any));
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.GONDOR, 1, Filters.any));
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.GONDOR, 1, Filters.any));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
