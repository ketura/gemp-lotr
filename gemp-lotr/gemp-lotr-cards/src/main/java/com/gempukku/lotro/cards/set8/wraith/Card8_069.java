package com.gempukku.lotro.cards.set8.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.cards.modifiers.CantHealModifier;
import com.gempukku.lotro.cards.modifiers.MayNotBearModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: Archery: Spot your enduring or mounted Nazgul to transfer this condition from your support area to
 * an unbound companion. Discard a mount borne by that companion. Bearer cannot heal or bear mounts.
 */
public class Card8_069 extends AbstractPermanent {
    public Card8_069() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Black Dart");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && self.getZone() == Zone.SUPPORT
                && PlayConditions.canSpot(game, Filters.owner(playerId), Race.NAZGUL, Filters.or(Keyword.ENDURING, Filters.mounted))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose an unbound companion", Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            final TransferPermanentEffect transferPermanentEffect = new TransferPermanentEffect(self, card);
                            action.insertEffect(
                                    transferPermanentEffect);
                            if (transferPermanentEffect.isPlayableInFull(game))
                                action.appendEffect(
                                        new DiscardCardsFromPlayEffect(self, PossessionClass.MOUNT, Filters.attachedTo(card)));
                        }
                    });
        }
        return null;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantHealModifier(self, Filters.hasAttached(self)));
        modifiers.add(
                new MayNotBearModifier(self, Filters.hasAttached(self), PossessionClass.MOUNT));
        return modifiers;
    }
}
