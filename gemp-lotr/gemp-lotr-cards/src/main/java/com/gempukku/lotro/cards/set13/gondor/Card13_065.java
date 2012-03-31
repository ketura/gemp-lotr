package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 5
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 5
 * Resistance: 6
 * Game Text: Knight. To play, spot 2 [GONDOR] knights or exert 2 [GONDOR] Men. Each of your [GONDOR] companions is
 * resistance +1. Maneuver: Remove a [GONDOR] token to make an unbound companion resistance +1 until the regroup phase.
 */
public class Card13_065 extends AbstractCompanion {
    public Card13_065() {
        super(5, 8, 5, 6, Culture.GONDOR, Race.MAN, null, "Elendil", "High-King of Gondor", true);
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && (PlayConditions.canSpot(game, 2, Culture.GONDOR, Keyword.KNIGHT) || PlayConditions.canExert(self, game, 2, Culture.GONDOR, Race.MAN));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new SpotEffect(2, Culture.GONDOR, Keyword.KNIGHT) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Spot 2 GONDOR knights";
                    }
                });
        possibleCosts.add(
                new ChooseAndExertCharactersEffect(playCardAction, playerId, 2, 2, Culture.GONDOR, Race.MAN));
        playCardAction.appendCost(
                new ChoiceEffect(playCardAction, playerId, possibleCosts));
        return playCardAction;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ResistanceModifier(self, Filters.and(Filters.owner(self.getOwner()), Culture.GONDOR, CardType.COMPANION), 1);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canRemoveTokens(game, Token.GONDOR, 1, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.GONDOR, 1, Filters.any));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an unbound companion", Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new ResistanceModifier(self, card, 1), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
