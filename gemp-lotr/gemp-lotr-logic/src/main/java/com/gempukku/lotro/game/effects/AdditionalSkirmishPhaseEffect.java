package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.game.actions.lotronly.SkirmishPhaseAction;

import java.util.Set;

public class AdditionalSkirmishPhaseEffect extends UnrespondableEffect {
    private final PhysicalCard _fpCard;
    private final Set<PhysicalCard> _shadowCards;

    public AdditionalSkirmishPhaseEffect(PhysicalCard fpCard, Set<PhysicalCard> shadowCards) {
        _fpCard = fpCard;
        _shadowCards = shadowCards;
    }

    @Override
    protected void doPlayEffect(DefaultGame game) {
        final Phase currentPhase = game.getGameState().getCurrentPhase();
        final Skirmish skirmish = game.getGameState().getSkirmish();
        final boolean fierce = game.getGameState().isFierceSkirmishes();
        final boolean extra = game.getGameState().isExtraSkirmishes();

        if (skirmish != null)
            game.getGameState().finishSkirmish();

        final SkirmishPhaseAction skirmishPhaseAction = new SkirmishPhaseAction(_fpCard, _shadowCards);
        // We have to reset the phase, to whatever it was before
        skirmishPhaseAction.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        // We might have to restart an existing skirmish
                        if (skirmish != null)
                            game.getGameState().restartSkirmish(skirmish);
                        game.getGameState().setCurrentPhase(currentPhase);
                        game.getGameState().setFierceSkirmishes(fierce);
                        game.getGameState().setExtraSkirmishes(extra);
                    }
                });

        game.getGameState().setFierceSkirmishes(false);
        game.getGameState().setExtraSkirmishes(true);
        game.getActionsEnvironment().addActionToStack(
                skirmishPhaseAction);
    }
}
