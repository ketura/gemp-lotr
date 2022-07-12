package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 Hobbits. Each time you play a follower, you may heal a companion (or heal Sam twice).
 */
public class Card13_154 extends AbstractPermanent {
    public Card13_154() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.SHIRE, "New Chapter");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.HOBBIT);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.owner(playerId), CardType.FOLLOWER)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, CardType.COMPANION) {
                        @Override
                        protected void forEachCardChosenToHealCallback(PhysicalCard character) {
                            if (character.getBlueprint().getTitle().equals("Sam"))
                                action.appendEffect(
                                        new HealCharactersEffect(self, self.getOwner(), character));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
