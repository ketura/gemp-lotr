package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.LinkedList;
import java.util.List;

/**
 * The Eagles Are Coming [Gandalf]
 * Event • Skirmish
 * Twilight Cost 3
 * 'Spell.
 * Exert Gandalf to play a [Gandalf] follower from your draw deck or discard pile.
 * You may attach a [Gandalf] follower from your support area to a companion (without paying the aid cost).'
 */
public class Card31_014 extends AbstractEvent {
    public Card31_014() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "The Eagles Are Coming", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.name("Gandalf"));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);

        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Gandalf")));

        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.GANDALF, CardType.FOLLOWER));
        possibleEffects.add(
                new ChooseAndPlayCardFromDeckEffect(playerId, Culture.GANDALF, CardType.FOLLOWER));

        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));

        SubCostToEffectAction subAction = new SubCostToEffectAction(action);
        subAction.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a [GANDALF] follower", Culture.GANDALF, CardType.FOLLOWER, Zone.SUPPORT) {
            @Override
            protected void cardSelected(LotroGame game, final PhysicalCard follower) {
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION) { 
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard companion) {
                        action.appendEffect(new TransferPermanentEffect(follower, companion));
                    }
                });
            }
        });
		
        action.appendEffect(
                new OptionalEffect(action, playerId,
                new StackActionEffect(subAction) {
            @Override
            public String getText(LotroGame game) {
                return "Attach a [GANDALF] follower";
            }
	}));
    return action;
    }
}
