package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Bearer must be an Elf companion. Archery: If bearer is an archer, exert bearer to make the
 * fellowship archery total +1.
 */
public class Card1_062 extends AbstractAttachable {
    public Card1_062() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.ELVEN, "The Splendor of Their Banners", "1_62", true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {

            Filter validTargetFilter = Filters.and(Filters.keyword(Keyword.ELF), Filters.type(CardType.COMPANION));

            List<Action> actions = new LinkedList<Action>();
            appendAttachCardAction(actions, game, self, validTargetFilter);
            return actions;
        }

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.ARCHERY, self)
                && game.getModifiersQuerying().hasKeyword(game.getGameState(), self.getAttachedTo(), Keyword.ARCHER)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            CostToEffectAction action = new CostToEffectAction(self, "Exert bearer to make the fellowship archery total +1");
            action.addCost(new ExertCharacterEffect(self.getAttachedTo()));
            action.addEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 1), Phase.ARCHERY));

            return Collections.singletonList(action);
        }
        return null;
    }
}
