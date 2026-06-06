package io.github.NumberFactory.model;

import java.util.List;

public class SequenceGoal {

    private final String title;
    private final String description;
    private final List<Integer> expectedSequence;

    private int currentIndex = 0;
    private boolean failed = false;

    public SequenceGoal(String title, String description, List<Integer> expectedSequence) {
        this.title = title;
        this.description = description;
        this.expectedSequence = expectedSequence;
    }

    public void submitValue(int value) {
        if (failed || isComplete()) {
            return;
        }

        if (expectedSequence.get(currentIndex) == value) {
            currentIndex++;
        }
        else {
            failed = true;
        }
    }

    public boolean isComplete() {
        return !failed && currentIndex >= expectedSequence.size();
    }

    public boolean isFailed() {
        return failed;
    }

    public void reset() {
        currentIndex = 0;
        failed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Integer> getExpectedSequence() {
        return expectedSequence;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
