package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: Shadows
 * Twilight Cost: 3
 * Type: Site
 * Game Text: Plains. Until the regroup phase, each minion skirmishing a [ROHAN] companion loses fierce and cannot gain
 * fierce.
 */
public class Card11_243 extends AbstractShadowsSite {
    public Card11_243() {
        super("Harrowdale", 3, Direction.RIGHT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (game.getModifiersQuerying().getUntilStartOfPhaseLimitCounter(self, Phase.REGROUP).getUsedLimit() < 1
                && !game.getGameState().getCurrentPhase().equals(Phase.REGROUP)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            game.getModifiersQuerying().getUntilStartOfPhaseLimitCounter(self, Phase.REGROUP).incrementToLimit(1, 1);
                        }
                    });
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            game.getActionsEnvironment().addUntilStartOfPhaseActionProxy(
                                    new AbstractActionProxy() {
                                        private final Set<Integer> _minionsMarked = new HashSet<>();

                                        @Override
                                        public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                            Set<PhysicalCard> toLose = new HashSet<>();
                                            final Set<Integer> toLoseInts = new HashSet<>();
                                            for (PhysicalCard minion : Filters.filterActive(game, Filters.inSkirmishAgainst(Culture.ROHAN, CardType.COMPANION))) {
                                                if (!_minionsMarked.contains(minion.getCardId())) {
                                                    toLose.add(minion);
                                                    toLoseInts.add(minion.getCardId());
                                                }
                                            }

                                            if (toLose.size() > 0) {
                                                RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                action.appendEffect(
                                                        new UnrespondableEffect() {
                                                            @Override
                                                            protected void doPlayEffect(LotroGame game) {
                                                                _minionsMarked.addAll(toLoseInts);
                                                            }
                                                        });
                                                action.appendEffect(
                                                        new AddUntilStartOfPhaseModifierEffect(
                                                                new RemoveKeywordModifier(self, Filters.in(toLose), Keyword.FIERCE), Phase.REGROUP));
                                                return Collections.singletonList(action);
                                            }
                                            return null;
                                        }
                                    }, Phase.REGROUP);
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
