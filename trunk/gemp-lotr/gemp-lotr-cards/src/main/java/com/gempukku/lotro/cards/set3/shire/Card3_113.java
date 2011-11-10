package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RemoveBurdenResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot 2 [SHIRE] companions. Plays to your support area. Each time you remove a burden (except by
 * a Hobbit's game text), you may heal a companion.
 */
public class Card3_113 extends AbstractPermanent {
    public Card3_113() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "The Shire Countryside");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SHIRE), Filters.type(CardType.COMPANION)) >= 2;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.REMOVE_BURDEN) {
            RemoveBurdenResult removeBurdenResult = (RemoveBurdenResult) effectResult;
            PhysicalCard source = removeBurdenResult.getSource();
            if (source != null && source.getOwner().equals(playerId) && source.getBlueprint().getRace() != Race.HOBBIT) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, Filters.type(CardType.COMPANION)));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
