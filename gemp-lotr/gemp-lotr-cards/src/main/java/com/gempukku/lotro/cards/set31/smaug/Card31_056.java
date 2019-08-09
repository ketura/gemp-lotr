package com.gempukku.lotro.cards.set31.smaug;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.TransferPermanentEffect;
import com.gempukku.lotro.logic.modifiers.AddActionToCardModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * • The Arkenstone, King's Jewel [Smaug culture]
 * Artifact
 * Twilight Cost 2
 * Strength bonus: -2
 * 'Plays on Bilbo.
 * At sites 7 to 9, wound bearer at the end of the maneuver phase. (If bearer is Bilbo, add a doubt instead.)
 * Thorin gains this ability: "Maneuver: Add a doubt to transfer the Arkenstone to Thorin (or add 3 doubts to discard it)"
 */
public class Card31_056 extends AbstractAttachable {
    public Card31_056() {
        super(Side.SHADOW, CardType.ARTIFACT, 2, Culture.GUNDABAD, null, "The Arkenstone", "King's Jewel", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Bilbo");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, final PhysicalCard arkenstone) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(arkenstone, Filters.hasAttached(arkenstone), -2));
        modifiers.add(
                new AddActionToCardModifier(arkenstone, null, Filters.name("Thorin")) {
                    @Override
                    public List<? extends ActivateCardAction> getExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard thorin) {
                        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, thorin)
                                && PlayConditions.canAddBurdens(game, thorin.getOwner(), thorin)) {
                            ActivateCardAction transferAction = new ActivateCardAction(arkenstone);
                            transferAction.setText("Add a doubt to transfer The Arkenstone to Thorin");
                            transferAction.appendCost(
                                    new AddBurdenEffect(thorin.getOwner(), thorin, 1));
                            transferAction.appendEffect(
                                    new TransferPermanentEffect(
                                            arkenstone, thorin));

                            ActivateCardAction discardAction = new ActivateCardAction(arkenstone);
                            discardAction.setText("Add 3 doubts to discard The Arkenstone");
                            discardAction.appendCost(
                                    new AddBurdenEffect(thorin.getOwner(), thorin, 3));
                            discardAction.appendEffect(
                                    new DiscardCardsFromPlayEffect(thorin.getOwner(), thorin, arkenstone));
                            return Arrays.asList(transferAction, discardAction);
                        }
                        return null;
                    }

                    @Override
                    protected ActivateCardAction createExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard matchingCard) {
                        // Ignore
                        return null;
                    }
                });
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.endOfPhase(game, effectResult, Phase.MANEUVER)
                && (game.getGameState().getCurrentSiteNumber() == 7 || game.getGameState().getCurrentSiteNumber()==9)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            if (Filters.name("Bilbo").accepts(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
                action.appendEffect(
                        new AddBurdenEffect(self.getOwner(), self, 1));
            } else {
                action.appendEffect(
                        new WoundCharactersEffect(self, Filters.hasAttached(self)));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
