package com.gempukku.lotro.cards.set13.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 6
 * Type: Condition • Support Area
 * Game Text: Toil 2. (For each [ELVEN] character you exert when playing this, its twilight cost is -2.)
 * Archery: Discard this from play and spot 3 Elf archers to discard a minion from play. Each of those archers loses
 * archer and cannot gain archer.
 */
public class Card13_026 extends AbstractPermanent {
    public Card13_026() {
        super(Side.FREE_PEOPLE, 6, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Take Up the Bow");
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canSpot(game, 3, Race.ELF, Keyword.ARCHER)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChooseActiveCardsEffect(self, playerId, "Choose Elf archers", 3, 3, Race.ELF, Keyword.ARCHER) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new RemoveKeywordModifier(self, Filters.in(cards), Keyword.ARCHER), Phase.ARCHERY));
                        }
                    });
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
