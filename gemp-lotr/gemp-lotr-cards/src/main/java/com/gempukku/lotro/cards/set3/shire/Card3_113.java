package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
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
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, "The Shire Countryside");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Culture.SHIRE, CardType.COMPANION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.REMOVE_BURDEN) {
            RemoveBurdenResult removeBurdenResult = (RemoveBurdenResult) effectResult;
            PhysicalCard source = removeBurdenResult.getSource();
            if (source != null && removeBurdenResult.getPerformingPlayerId().equals(playerId) && source.getBlueprint().getRace() != Race.HOBBIT) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
