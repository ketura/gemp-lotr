package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Resistance: -1
 * Game Text: Response: If a companion loses a skirmish involving a [MEN] minion, transfer this condition from your
 * support area to that companion. Limit 1 per companion. Minions cannot take wounds during skirmishes involving bearer.
 */
public class Card13_093 extends AbstractPermanent {
    public Card13_093() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Harmless");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ResistanceModifier(self, Filters.hasAttached(self), -1));
        modifiers.add(
                new CantTakeWoundsModifier(self, new SpotCondition(Filters.hasAttached(self), Filters.inSkirmish), CardType.MINION));
        return modifiers;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmishInvolving(game, effectResult,
                Filters.and(CardType.COMPANION, Filters.not(Filters.hasAttached(Filters.name(getName())))),
                Filters.and(Culture.MEN, CardType.MINION))
                && self.getZone() == Zone.SUPPORT) {
            final PhysicalCard loser = ((CharacterLostSkirmishResult) effectResult).getLoser();
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new TransferPermanentEffect(self, loser));
            return Collections.singletonList(action);
        }
        return null;
    }
}
