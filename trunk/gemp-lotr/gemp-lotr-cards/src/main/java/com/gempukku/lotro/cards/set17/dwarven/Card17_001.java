package com.gempukku.lotro.cards.set17.dwarven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession • Armor
 * Game Text: Bearer must be a Dwarf. Each time bearer is about to take a wound, you may add a threat to prevent that.
 * Regroup: Discard this possession to take up to 2 other [DWARVEN] possessions into hand from your discard pile.
 */
public class Card17_001 extends AbstractAttachable {
    public Card17_001() {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, 2, Culture.DWARVEN, PossessionClass.ARMOR, "Armor of Khazad");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.hasAttached(self))
                && PlayConditions.canAddThreat(game, self, 1)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new AddThreatsEffect(playerId, self, 1));
            action.appendEffect(
                    new PreventCardEffect((WoundCharactersEffect) effect, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 0, 2, Culture.DWARVEN, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
