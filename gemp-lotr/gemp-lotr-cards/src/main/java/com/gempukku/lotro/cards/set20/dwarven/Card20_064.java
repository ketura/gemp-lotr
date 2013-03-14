package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * Retribution
 * Dwarven	Event • Skirmish
 * Make a Dwarf strength +2. If that Dwarf wins this skirmish, you may discard a support area condition or possession.
 */
public class Card20_064 extends AbstractEvent {
    public Card20_064() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Retribution", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Race.DWARF) {
                    @Override
                    protected void selectedCharacterCallback(final PhysicalCard selectedCharacter) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                                                if (TriggerConditions.winsSkirmish(game, effectResult, selectedCharacter)
                                                        && playerId.equals(selectedCharacter.getOwner())) {
                                                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                                                    action.setVirtualCardAction(true);
                                                    action.appendEffect(
                                                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1,
                                                                    Filters.or(Filters.and(CardType.CONDITION, Keyword.SUPPORT_AREA), CardType.POSSESSION)));
                                                    return Collections.singletonList(action);
                                                }
                                                return null;
                                            }
                                        }));
                    }
                });
        return action;
    }
}
