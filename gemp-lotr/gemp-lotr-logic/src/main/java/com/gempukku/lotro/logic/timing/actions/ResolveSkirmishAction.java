package com.gempukku.lotro.logic.timing.actions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.effects.OverwhelmedEffect;
import com.gempukku.lotro.logic.effects.SkirmishResolvedEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ResolveSkirmishAction implements Action {
    private LotroGame _game;
    private Skirmish _skirmish;

    Iterator<Effect> _effects;

    public ResolveSkirmishAction(LotroGame game, Skirmish skirmish) {
        _game = game;
        _skirmish = skirmish;
    }

    @Override
    public Phase getActionTimeword() {
        return null;
    }

    @Override
    public void setActionTimeword(Phase phase) {
    }

    @Override
    public PhysicalCard getActionSource() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Resolving skirmish";
    }

    @Override
    public Effect nextEffect(LotroGame game) {
        if (_effects == null) {
            _effects = resolveSkirmish();
        }

        if (_effects.hasNext())
            return _effects.next();
        else
            return null;
    }

    private Iterator<Effect> resolveSkirmish() {
        List<Effect> effects = new LinkedList<Effect>();

        final GameState gameState = _game.getGameState();

        PhysicalCard fp = _skirmish.getFellowshipCharacter();
        int fpStrength = 0;
        if (fp != null)
            fpStrength = _game.getModifiersQuerying().getStrength(gameState, fp);

        int shadowStrength = 0;
        for (PhysicalCard minion : _skirmish.getShadowCharacters())
            shadowStrength += _game.getModifiersQuerying().getStrength(gameState, minion);

        if (_game.getModifiersQuerying().isOverwhelmedByStrength(gameState, fp, fpStrength, shadowStrength))
            effects.add(new OverwhelmedEffect(_skirmish.getShadowCharacters(), fpList(fp)));
        else if (shadowStrength >= fpStrength)
            effects.add(new SkirmishResolvedEffect(_skirmish.getShadowCharacters(), fpList(fp)));
        else if (fpStrength >= 2 * shadowStrength)
            effects.add(new OverwhelmedEffect(fpList(fp), _skirmish.getShadowCharacters()));
        else
            effects.add(new SkirmishResolvedEffect(fpList(fp), _skirmish.getShadowCharacters()));

        effects.add(new UnrespondableEffect() {
            @Override
            public void doPlayEffect(LotroGame game) {
                gameState.finishSkirmish();
            }
        });

        return effects.iterator();
    }

    private List<PhysicalCard> fpList(PhysicalCard card) {
        if (card != null)
            return Collections.singletonList(card);
        else
            return Collections.emptyList();
    }
}
