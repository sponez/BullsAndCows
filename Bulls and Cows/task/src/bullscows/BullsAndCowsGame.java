package bullscows;

import java.util.InputMismatchException;

public class BullsAndCowsGame {
    private final BullsAndCowsGameController controller;
    private BullsAndCowsCode code;

    public BullsAndCowsGame() {
        controller = new BullsAndCowsGameController(this);
        code = null;
    }

    public BullsAndCowsCode getCode() {
        return code;
    }

    public void setCode(BullsAndCowsCode code) {
        this.code = code;
    }

    public void start() {
        try {
            controller.generateRandomCode();
        } catch (Exception exception) {
            System.out.print(exception.getMessage());
            return;
        }

        controller.executeGame();
    }
}
