package com.gempukku.lotro.cards.set7.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Twilight Cost: 2
 * Type: Site
 * Site: 4K
 * Game Text: Skirmish: Spot your minion and remove 2 burdens to make that minion damage +1.
 */
public class Card7_342 extends AbstractSite {
    public Card7_342() {
        super("Osgiliath Fallen", SitesBlock.KING, 4, 2, Direction.RIGHT);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Filters.owner(playerId), CardType.MINION)
                && PlayConditions.canRemoveBurdens(game, self, 2)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveBurdenEffect(playerId, self, 2));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose your minion", Filters.owner(playerId), CardType.MINION) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.DAMAGE, 1)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
