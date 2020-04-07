package com.gempukku.lotro.cards.set6.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a Nazgul. While bearer is Úlairë Toldëa, each time he wins a skirmish the Free Peoples
 * player must discard a Free Peoples possession or a Free Peoples condition.
 */
public class Card6_085 extends AbstractAttachable {
    public Card6_085() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.WRAITH, PossessionClass.HAND_WEAPON, "Sword of Dol Guldur", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())
                && PlayConditions.canSpot(game, Filters.hasAttached(self), Filters.name(Names.toldea))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Side.FREE_PEOPLE, Filters.or(CardType.POSSESSION, CardType.CONDITION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
