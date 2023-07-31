package com.gempukku.lotro.processes.lotronly.skirmish;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

import java.util.HashSet;
import java.util.Set;

public class Skirmish {
    private LotroPhysicalCard _fellowshipCharacter;
    private final Set<LotroPhysicalCard> _shadowCharacters;
    private boolean _cancelled;

    private Evaluator _fpStrengthOverrideEvaluator;
    private Evaluator _shadowStrengthOverrideEvaluator;

    private final Set<LotroPhysicalCard> _removedFromSkirmish = new HashSet<>();

    public Skirmish(LotroPhysicalCard fellowshipCharacter, Set<LotroPhysicalCard> shadowCharacters) {
        _fellowshipCharacter = fellowshipCharacter;
        _shadowCharacters = shadowCharacters;
    }

    public Evaluator getFpStrengthOverrideEvaluator() {
        return _fpStrengthOverrideEvaluator;
    }

    public void setFpStrengthOverrideEvaluator(Evaluator fpStrengthOverrideEvaluator) {
        _fpStrengthOverrideEvaluator = fpStrengthOverrideEvaluator;
    }

    public Evaluator getShadowStrengthOverrideEvaluator() {
        return _shadowStrengthOverrideEvaluator;
    }

    public void setShadowStrengthOverrideEvaluator(Evaluator shadowStrengthOverrideEvaluator) {
        _shadowStrengthOverrideEvaluator = shadowStrengthOverrideEvaluator;
    }

    public LotroPhysicalCard getFellowshipCharacter() {
        return _fellowshipCharacter;
    }

    public Set<LotroPhysicalCard> getShadowCharacters() {
        return _shadowCharacters;
    }

    public void setFellowshipCharacter(LotroPhysicalCard fellowshipCharacter) {
        _fellowshipCharacter = fellowshipCharacter;
    }

    public void addRemovedFromSkirmish(LotroPhysicalCard loser) {
        _removedFromSkirmish.add(loser);
    }

    public Set<LotroPhysicalCard> getRemovedFromSkirmish() {
        return _removedFromSkirmish;
    }

    public void cancel() {
        _cancelled = true;
    }

    public boolean isCancelled() {
        return _cancelled;
    }
}
