package com.gempukku.lotro.cards.set18.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotCultureTokensCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 3
 * Vitality: 3
 * Resistance: 5
 * Game Text: While you can spot 2 [GANDALF] tokens, each of your [GANDALF] Men is strength +2. Regroup: Discard
 * a follower from play to choose one: draw a card; or take a [GANDALF] skirmish event from your discard pile into hand.
 */
public class Card18_022 extends AbstractCompanion {
    public Card18_022() {
        super(2, 3, 3, 5, Culture.GANDALF, Race.MAN, null, "Librarian", "Keeper of Ancient Texts", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Culture.GANDALF, Race.MAN), new CanSpotCultureTokensCondition(2, Token.GANDALF), 2);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.FOLLOWER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.FOLLOWER));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new DrawCardsEffect(action, playerId, 1));
            possibleEffects.add(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF, CardType.EVENT, Keyword.SKIRMISH) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Take a GANDALF skirmish event from your discard pile into hand";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
