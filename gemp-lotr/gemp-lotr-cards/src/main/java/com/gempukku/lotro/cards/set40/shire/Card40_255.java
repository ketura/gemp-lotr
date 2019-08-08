package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Longbottom Leaf, Hornblower's Crop
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession - Support Area
 * Card Number: 1C255
 * Game Text: Pipeweed. When you play this possession, you may exert a Hobbit companion to heal a Shire ally.
 */
public class Card40_255 extends AbstractPermanent {
    public Card40_255() {
        super(Side.FREE_PEOPLE, 1, CardType.POSSESSION, Culture.SHIRE, Zone.SUPPORT, "Longbottom Leaf", "Hornblower's Crop", false);
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
        && PlayConditions.canExert(self, game, Race.HOBBIT, CardType.COMPANION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, CardType.COMPANION));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.ALLY, Keyword.SHIRE));
            return Collections.singletonList(action);
        }
        return null;
    }
}
