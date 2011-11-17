package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.PreventEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 [GONDOR] Men. The twilight cost of each of your [GONDOR] fortifications is -1.
 * Response: If an opponent is about to control a site, discard 2 [GONDOR] fortifications to prevent this.
 */
public class Card7_122 extends AbstractPermanent {
    public Card7_122() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Strong and Old", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Culture.GONDOR, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, Filters.and(Filters.owner(self.getOwner()), Culture.GONDOR, Keyword.FORTIFICATION), -1));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isTakingControlOfSite(effect, game, Filters.not(Filters.owner(playerId)))
                && PlayConditions.canDiscardFromPlay(self, game, 2, Culture.GONDOR, Keyword.FORTIFICATION)) {
            TakeControlOfASiteEffect takeControlEffect = (TakeControlOfASiteEffect) effect;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Culture.GONDOR, Keyword.FORTIFICATION));
            action.appendEffect(
                    new PreventEffect(takeControlEffect));
            return Collections.singletonList(action);
        }
        return null;
    }
}
