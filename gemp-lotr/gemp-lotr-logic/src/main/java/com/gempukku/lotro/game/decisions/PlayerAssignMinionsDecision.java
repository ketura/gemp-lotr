package com.gempukku.lotro.game.decisions;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;

import java.util.*;

public abstract class PlayerAssignMinionsDecision extends AbstractAwaitingDecision {
    private final List<LotroPhysicalCard> _freeCharacters;
    private final List<LotroPhysicalCard> _minions;

    public PlayerAssignMinionsDecision(int id, String text, Collection<LotroPhysicalCard> freeCharacters, Collection<LotroPhysicalCard> minions) {
        super(id, text, AwaitingDecisionType.ASSIGN_MINIONS);
        _freeCharacters = new LinkedList<>(freeCharacters);
        _minions = new LinkedList<>(minions);
        setParam("freeCharacters", getCardIds(_freeCharacters));
        setParam("minions", getCardIds(_minions));
    }

    protected Map<LotroPhysicalCard, Set<LotroPhysicalCard>> getAssignmentsBasedOnResponse(String response) throws DecisionResultInvalidException {
        Map<LotroPhysicalCard, Set<LotroPhysicalCard>> assignments = new HashMap<>();
        if (response.equals(""))
            return assignments;

        Set<LotroPhysicalCard> assignedMinions = new HashSet<>();

        try {
            String[] groups = response.split(",");
            for (String group : groups) {
                String[] cardIds = group.split(" ");
                LotroPhysicalCard freeCard = getCardId(_freeCharacters, Integer.parseInt(cardIds[0]));
                if (assignments.containsKey(freeCard))
                    throw new DecisionResultInvalidException();

                Set<LotroPhysicalCard> minions = new HashSet<>();
                for (int i = 1; i < cardIds.length; i++) {
                    LotroPhysicalCard minion = getCardId(_minions, Integer.parseInt(cardIds[i]));
                    if (assignedMinions.contains(minion))
                        throw new DecisionResultInvalidException();
                    minions.add(minion);
                    assignedMinions.add(minion);
                }

                assignments.put(freeCard, minions);
            }
        } catch (NumberFormatException exp) {
            throw new DecisionResultInvalidException();
        }

        return assignments;
    }

    private LotroPhysicalCard getCardId(List<LotroPhysicalCard> physicalCards, int cardId) throws DecisionResultInvalidException {
        for (LotroPhysicalCard physicalCard : physicalCards) {
            if (physicalCard.getCardId() == cardId)
                return physicalCard;
        }
        throw new DecisionResultInvalidException();
    }

    private String[] getCardIds(List<LotroPhysicalCard> cards) {
        String[] result = new String[cards.size()];
        for (int i = 0; i < cards.size(); i++)
            result[i] = String.valueOf(cards.get(i).getCardId());
        return result;
    }
}
