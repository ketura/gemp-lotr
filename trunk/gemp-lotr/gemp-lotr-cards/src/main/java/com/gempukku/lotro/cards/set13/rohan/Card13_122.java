package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Event • Response
 * Game Text: If Theoden is about to take a wound in a skirmish, you may discard from play Eomer or Theodred to prevent
 * that. If Theodred is about to take a wound in a skirmish, you may discard from play Eomer or Eowyn to prevent that.
 */
public class Card13_122 extends AbstractResponseEvent {
    public Card13_122() {
        super(Side.FREE_PEOPLE, 2, Culture.ROHAN, "Bitter Tidings");
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        List<PlayEventAction> actions = new LinkedList<PlayEventAction>();
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && TriggerConditions.isGettingWounded(effect, game, Filters.name(Names.theoden))
                && PlayConditions.canDiscardFromPlay(self, game, Filters.or(Filters.name(Names.eomer), Filters.name("Theodred")))) {
            PlayEventAction action = new PlayEventAction(self);
            action.setText("Prevent wound to Theoden");
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.or(Filters.name(Names.eomer), Filters.name("Theodred"))));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose Theoden", Filters.name(Names.theoden)));
            actions.add(action);
        }
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && TriggerConditions.isGettingWounded(effect, game, Filters.name("Theodred"))
                && PlayConditions.canDiscardFromPlay(self, game, Filters.or(Filters.name(Names.eomer), Filters.name(Names.eowyn)))) {
            PlayEventAction action = new PlayEventAction(self);
            action.setText("Prevent wound to Theodred");
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.or(Filters.name(Names.eomer), Filters.name(Names.eowyn))));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose Theodred", Filters.name("Theodred")));
            actions.add(action);
        }

        return actions;
    }
}
