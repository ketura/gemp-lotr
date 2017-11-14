package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: Fellowship: Exert an Elf to heal a Man. Regroup: Exert a Man and discard this artifact to play an Elf from
 * your draw deck or discard pile.
 */
public class Card31_003 extends AbstractPermanent {
    public Card31_003() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.DWARVEN, Zone.SUPPORT, "Emeralds of Girion", null, true);
	}


    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
	    final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
	            new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
	    action.appendEffect(
	            new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Race.MAN));
	    return Collections.singletonList(action);
	}
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
	        && PlayConditions.canExert(self, game, Race.MAN)) {
	    final ActivateCardAction action = new ActivateCardAction(self);
	    action.appendCost(
	        new ChooseAndExertCharactersEffect(action, playerId, 1, 1,  Race.MAN));
	    action.appendCost(
	        new SelfDiscardEffect(self));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Race.ELF) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play an Elf from your draw deck";
                        }
                    });
            if (PlayConditions.canPlayFromDiscard(playerId, game, Race.ELF)) {
                possibleEffects.add(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, Race.ELF) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Play an Elf from your discard pile";
                            }
                        });
            }
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
	    return Collections.singletonList(action);
        }
	return null;
    }
}
