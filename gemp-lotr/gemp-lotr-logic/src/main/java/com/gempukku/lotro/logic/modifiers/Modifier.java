package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Modifier {
    public PhysicalCard getSource();

    public String getText(LotroGame game, PhysicalCard self);

    public ModifierEffect getModifierEffect();

    public boolean isNonCardTextModifier();

    public Condition getCondition();

    public boolean affectsCard(LotroGame game, PhysicalCard physicalCard);

    public boolean hasRemovedText(LotroGame game, PhysicalCard physicalCard);

    public boolean hasKeyword(LotroGame game, PhysicalCard physicalCard, Keyword keyword);

    public boolean hasSignet(LotroGame game, PhysicalCard physicalCard, Signet signet);

    public int getKeywordCountModifier(LotroGame game, PhysicalCard physicalCard, Keyword keyword);

    public boolean appliesKeywordModifier(LotroGame game, PhysicalCard modifierSource, Keyword keyword);

    public boolean isKeywordRemoved(LotroGame game, PhysicalCard physicalCard, Keyword keyword);

    public int getStrengthModifier(LotroGame game, PhysicalCard physicalCard);

    public boolean appliesStrengthBonusModifier(LotroGame game, PhysicalCard modifierSource, PhysicalCard modifierTaget);

    public int getVitalityModifier(LotroGame game, PhysicalCard physicalCard);

    public int getResistanceModifier(LotroGame game, PhysicalCard physicalCard);

    public int getMinionSiteNumberModifier(LotroGame game, PhysicalCard physicalCard);

    public boolean isAdditionalCardTypeModifier(LotroGame game, PhysicalCard physicalCard, CardType cardType);

    public int getTwilightCostModifier(LotroGame game, PhysicalCard physicalCard, boolean ignoreRoamingPenalty);

    public int getPlayOnTwilightCostModifier(LotroGame game, PhysicalCard physicalCard, PhysicalCard target);

    public int getRoamingPenaltyModifier(LotroGame game, PhysicalCard physicalCard);

    public int getOverwhelmMultiplier(LotroGame game, PhysicalCard physicalCard);

    public boolean canTakeWounds(LotroGame game, Collection<PhysicalCard> woundSources, PhysicalCard physicalCard, int woundsAlreadyTakenInPhase, int woundsToTake);

    public boolean canTakeWoundsFromLosingSkirmish(LotroGame game, PhysicalCard physicalCard, Set<PhysicalCard> winners);

    public boolean canTakeArcheryWound(LotroGame game, PhysicalCard physicalCard);

    public boolean canBeExerted(LotroGame game, PhysicalCard source, PhysicalCard card);

    public boolean isAllyParticipateInArcheryFire(LotroGame game, PhysicalCard card);

    public boolean isAllyParticipateInSkirmishes(LotroGame game, Side sidePlayer, PhysicalCard card);

    public boolean isUnhastyCompanionAllowedToParticipateInSkirmishes(LotroGame game, PhysicalCard card);

    public boolean isAllyPreventedFromParticipatingInArcheryFire(LotroGame game, PhysicalCard card);

    public boolean isAllyPreventedFromParticipatingInSkirmishes(LotroGame game, Side sidePlayer, PhysicalCard card);

    public int getArcheryTotalModifier(LotroGame game, Side side);

    public int getMoveLimitModifier(LotroGame game);

    boolean addsTwilightForCompanionMove(LotroGame game, PhysicalCard companion);

    public boolean addsToArcheryTotal(LotroGame game, PhysicalCard card);

    public boolean canPlayAction(LotroGame game, String performingPlayer, Action action);

    public List<? extends ActivateCardAction> getExtraPhaseAction(LotroGame game, PhysicalCard card);

    public List<? extends Action> getExtraPhaseActionFromStacked(LotroGame game, PhysicalCard card);

    public boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card);

    public List<? extends Effect> getExtraCostsToPlay(LotroGame game, Action action, PhysicalCard card);

    public boolean canHavePlayedOn(LotroGame game, PhysicalCard playedCard, PhysicalCard target);

    public boolean canHaveTransferredOn(LotroGame game, PhysicalCard playedCard, PhysicalCard target);

    public boolean canBeTransferred(LotroGame game, PhysicalCard attachment);

    public boolean shouldSkipPhase(LotroGame game, Phase phase, String playerId);

    public boolean isValidAssignments(LotroGame game, Side Side, PhysicalCard companion, Set<PhysicalCard> minions);

    public boolean isValidAssignments(LotroGame game, Side Side, Map<PhysicalCard, Set<PhysicalCard>> assignments);

    public boolean isPreventedFromBeingAssignedToSkirmish(LotroGame game, Side sidePlayer, PhysicalCard card);

    public boolean canBeDiscardedFromPlay(LotroGame game, String performingPlayer, PhysicalCard card, PhysicalCard source);

    public boolean canBeLiberated(LotroGame game, String performingPlayer, PhysicalCard card, PhysicalCard source);

    public boolean canBeReturnedToHand(LotroGame game, PhysicalCard card, PhysicalCard source);

    public boolean canBeHealed(LotroGame game, PhysicalCard card);

    public boolean canAddBurden(LotroGame game, String performingPlayer, PhysicalCard source);

    public boolean canRemoveBurden(LotroGame game, PhysicalCard source);

    public boolean canRemoveThreat(LotroGame game, PhysicalCard source);

    public boolean canLookOrRevealCardsInHand(LotroGame game, String revealingPlayerId, String actingPlayerId);

    public boolean canDiscardCardsFromHand(LotroGame game, String playerId, PhysicalCard source);

    public boolean canDiscardCardsFromTopOfDeck(LotroGame game, String playerId, PhysicalCard source);

    public int getSpotCountModifier(LotroGame game, Filterable filter);

    public boolean hasFlagActive(LotroGame game, ModifierFlag modifierFlag);

    public boolean isSiteReplaceable(LotroGame game, String playerId);

    public boolean canPlaySite(LotroGame game, String playerId);

    public boolean shadowCanHaveInitiative(LotroGame game);

    public Side hasInitiative(LotroGame game);

    public int getInitiativeHandSizeModifier(LotroGame game);

    public boolean lostAllKeywords(LotroGame game, PhysicalCard card);

    Evaluator getFpSkirmishStrengthOverrideEvaluator(LotroGame game, PhysicalCard fpCharacter);

    public boolean canSpotCulture(LotroGame game, Culture culture, String playerId);

    public int getFPCulturesSpotCountModifier(LotroGame game, String playerId);

    int getSanctuaryHealModifier(LotroGame game);

    int getPotentialDiscount(LotroGame game, PhysicalCard discountCard);
}
