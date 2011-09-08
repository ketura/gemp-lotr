package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. When the fellowship moves to site 4 or 5, add (2) for each Dwarf companion.
 * Skirmish: Discard this condition to make your [MORIA] Orc strength +2.
 */
public class Card1_188 extends AbstractLotroCardBlueprint {
    public Card1_188() {
        super(Side.SHADOW, CardType.CONDITION, Culture.MORIA, "The Long Dark", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self)
                && PlayConditions.canPayForShadowCard(game, self, twilightModifier);
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT, twilightModifier);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0))
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SKIRMISH, "Discard this condition to make your MORIA Orc strength +2.");
            action.addCost(
                    new DiscardCardFromPlayEffect(self, self));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose your MORIA Orc", Filters.owner(self.getOwner()), Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC)) {
                        @Override
                        protected void cardSelected(PhysicalCard moriaOrc) {
                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(moriaOrc), 2), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && (game.getGameState().getCurrentSiteNumber() == 4 || game.getGameState().getCurrentSiteNumber() == 5)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Add (2) for each Dwarf companion.");
            int dwarfCompanions = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF), Filters.type(CardType.COMPANION));
            action.addEffect(new AddTwilightEffect(dwarfCompanions * 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
