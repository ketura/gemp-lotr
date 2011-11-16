package com.gempukku.lotro.logic.timing.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.ResolveSkirmishEffect;
import com.gempukku.lotro.logic.effects.StackActionEffect;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.results.EndOfPhaseResult;
import com.gempukku.lotro.logic.timing.results.SkirmishAboutToEndResult;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;

public class SkirmishPhaseAction extends SystemQueueAction {
    public SkirmishPhaseAction(final PhysicalCard character) {
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        game.getGameState().startSkirmish(character);
                        ((ModifiersLogic) game.getModifiersEnvironment()).removeStartOfPhase(Phase.SKIRMISH);
                        ((DefaultActionsEnvironment) game.getActionsEnvironment()).removeStartOfPhaseActionProxies(Phase.SKIRMISH);
                    }
                });
        appendEffect(
                new TriggeringResultEffect(null, new StartOfPhaseResult(Phase.SKIRMISH), "Start of skirmish phase"));
        appendEffect(
                new StackActionEffect(
                        new SkirmishActionProcedureAction()));
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        if (!game.getGameState().getSkirmish().isCancelled()) {
                            insertEffect(
                                    new ResolveSkirmishEffect(),
                                    new TriggeringResultEffect(new SkirmishAboutToEndResult(), "Skirmish about to end"));
                        }
                    }
                });
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        game.getGameState().finishSkirmish();
                    }
                });

        appendEffect(
                new TriggeringResultEffect(null, new EndOfPhaseResult(Phase.SKIRMISH), "End of skirmish phase"));
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        ((ModifiersLogic) game.getModifiersEnvironment()).removeEndOfPhase(Phase.SKIRMISH);
                        ((DefaultActionsEnvironment) game.getActionsEnvironment()).removeEndOfPhaseActionProxies(Phase.SKIRMISH);
                    }
                });
    }
}
