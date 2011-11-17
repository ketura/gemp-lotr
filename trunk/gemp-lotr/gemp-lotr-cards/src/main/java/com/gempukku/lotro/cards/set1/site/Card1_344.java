package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 2
 * Type: Site
 * Site: 4
 * Game Text: Underground. When the fellowship moves to Dwarrowdelf Chamber, Gimli or 2 other companions must exert.
 */
public class Card1_344 extends AbstractSite {
    public Card1_344() {
        super("Dwarrowdelf Chamber", Block.FELLOWSHIP, 4, 2, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_TO
                && game.getGameState().getCurrentSite() == self) {
            String fpPlayerId = game.getGameState().getCurrentPlayerId();
            boolean gimliCanExert = PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.gimli);
            boolean twoOtherCanExert = PlayConditions.canExertMultiple(self, game.getGameState(), game.getModifiersQuerying(), 1, 2, Filters.not(Filters.gimli), CardType.COMPANION);
            if (gimliCanExert && twoOtherCanExert) {
                final RequiredTriggerAction action = new RequiredTriggerAction(self);

                PhysicalCard gimli = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.gimli);
                List<Effect> possibleEffects = new LinkedList<Effect>();
                possibleEffects.add(new ExertCharactersEffect(self, gimli) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert Gimli";
                    }
                });
                possibleEffects.add(
                        new ChooseAndExertCharactersEffect(action, fpPlayerId, 2, 2, Filters.not(Filters.gimli), CardType.COMPANION) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Exert 2 other companions";
                            }
                        });
                action.appendEffect(
                        new ChoiceEffect(action, fpPlayerId, possibleEffects));
                return Collections.singletonList(action);
            }
            if (gimliCanExert) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                PhysicalCard gimli = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.gimli);
                action.appendEffect(new ExertCharactersEffect(self, gimli));
                return Collections.singletonList(action);
            }
            if (twoOtherCanExert) {
                final RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, fpPlayerId, 2, 2, Filters.not(Filters.gimli), CardType.COMPANION));
                return Collections.singletonList(action);
            }
        }

        return null;
    }
}
