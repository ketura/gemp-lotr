package com.gempukku.lotro.cards.set6.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Skirmish: Remove an [ELVEN] token to make a minion skirmishing Aragorn strength -3.
 * Skirmish: Remove a [DWARVEN] token to heal a Dwarf.
 * Regroup: If Aragorn is mounted, remove a [ROHAN] token to wound a minion twice.
 */
public class Card6_050 extends AbstractCompanion {
    public Card6_050() {
        super(4, 8, 4, 6, Culture.GONDOR, Race.MAN, Signet.THÉODEN, "Aragorn", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokens(game, Token.ELVEN, 1, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.setText("Remove ELVEN token...");
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.ELVEN, 1, Filters.any));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(self)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, -3), Phase.SKIRMISH));
                        }
                    });
            actions.add(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokens(game, Token.DWARVEN, 1, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.setText("Remove DWARVEN token...");
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.DWARVEN, 1, Filters.any));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Race.DWARF));
            actions.add(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && Filters.mounted.accepts(game.getGameState(), game.getModifiersQuerying(), self)
                && PlayConditions.canRemoveTokens(game, Token.ROHAN, 1, Filters.any)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.ROHAN, 1, Filters.any));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION));
            actions.add(action);
        }
        return actions;
    }
}
