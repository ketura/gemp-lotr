package com.gempukku.lotro.cards.set17.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: While the Ringbearer is assigned to a skirmish, each minion skirmishing a [ROHAN] companion loses hunter
 * and is unable to gain hunter. Skirmish: Exert a [ROHAN] companion to make that companion gain hunter 1 until
 * the start of the regroup phase.
 */
public class Card17_097 extends AbstractPermanent {
    public Card17_097() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "For Death and Glory");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new RemoveKeywordModifier(self,
                Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Culture.ROHAN, CardType.COMPANION)),
                new SpotCondition(Filters.ringBearer, Filters.assignedToSkirmish), Keyword.HUNTER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Culture.ROHAN, CardType.COMPANION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, CardType.COMPANION) {
                        @Override
                        protected void forEachCardExertedCallback(PhysicalCard character) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, character, Keyword.HUNTER, 1), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
