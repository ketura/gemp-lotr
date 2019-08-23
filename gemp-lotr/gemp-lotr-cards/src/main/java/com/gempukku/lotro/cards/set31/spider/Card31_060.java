package com.gempukku.lotro.cards.set31.spider;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Spider
 * Twilight Cost: 4
 * Type: Minion • Spider
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Fierce. The twilight cost of this minion is -1 for each Orc you spot. Skirmish: Discard an 
 * Orc to make a Spider damage +1.
 */
public class Card31_060 extends AbstractMinion {
    public Card31_060() {
        super(4, 9, 2, 5, Race.SPIDER, Culture.GUNDABAD, "Lazy Lob", null, true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -Filters.countActive(game, Race.ORC);
    }
	
	@Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.ORC));
            action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Spider", Race.SPIDER) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, card, Keyword.DAMAGE, 1)));
                    }
			});
            return Collections.singletonList(action);
        }
        return null;
    }
}