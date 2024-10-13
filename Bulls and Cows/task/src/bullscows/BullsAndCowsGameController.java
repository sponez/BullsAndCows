package bullscows;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BullsAndCowsGameController {
    private static class Message {
        public final static String ASK_CODE_LENGTH = "Please, enter the secret code's length: ";
        public final static String ASK_POSSIBLE_CHARS_AMOUNT = "Please, enter the secret code's length: ";

        public final static String CENSOR_CHAR = "*";
        public final static String CODE_PREPARED = "The secret is prepared: %s (%s).\n";

        public final static String LETS_START = "Okay, let's start a game!\n";
        public final static String TURN_NUMBER = "Turn %d: ";
        public final static String CONGRATULATIONS = "Congratulations! You guessed the secret code.\n";

        public static String gradeBuilder(int bulls, int cows) {
            StringBuilder grade = new StringBuilder("Grade: ");
            String noneGrade = "None";
            String bullsGrade = String.format("%d %s", bulls, bulls > 1 ? "bulls" : "bull");
            String and = " and ";
            String cowsGrade = String.format("%d %s", cows, cows > 1 ? "cows" : "cow");
            String stop = ".\n";

            if (bulls <= 0 && cows <= 0) {
                grade.append(noneGrade);
            } else if (bulls > 0 && cows > 0) {
                grade.append(bullsGrade).append(and).append(cowsGrade);
            } else if (bulls > 0) {
                grade.append(bullsGrade);
            } else {
                grade.append(cowsGrade);
            }

            return grade.append(stop).toString();
        }
    }

    private static class ErrorMessage {
        public final static String ERROR = "Error: %s";
        public final static String WARNING = "Warning: %s";

        public final static String IS_NOT_VALID_NUMBER = "\"%s\" isn't a valid number.\n";
        public final static String NUMBER_MUST_BE_BETWEEN = "The number must be between %d and %d inclusive!\n";

        public final static String CODE_CANNOT_BE_ACCEPTED = "The code cannot be accepted:\n";
        public final static String CODE_CONTAINS_ILLEGAL_CHAR = "\tThe code contains illegal symbol %c\n";
        public final static String CODE_LENGTH_MUST_BE = "\tThe code length must be equal %d\n";
    }

    private final static Scanner inputMaster = new Scanner(System.in);
    private final BullsAndCowsGame game;

    public BullsAndCowsGameController(BullsAndCowsGame game) {
        this.game = game;
    }

    private int inputCodeInfo(String question) throws InputMismatchException {
        String infoString;
        int infoInt;

        System.out.print(question);
        infoString = inputMaster.nextLine();

        try {
            infoInt = Integer.parseInt(infoString);
        } catch (NumberFormatException numberFormatException) {
            throw new InputMismatchException(
                String.format(
                    ErrorMessage.ERROR,
                    String.format(ErrorMessage.IS_NOT_VALID_NUMBER, infoString)
                )
            );
        }

        if (infoInt < BullsAndCowsCode.MIN_POSSIBLE_CHARS || infoInt > BullsAndCowsCode.MAX_POSSIBLE_CHARS) {
            throw new InputMismatchException(
                String.format(
                    ErrorMessage.ERROR,
                    String.format(
                        ErrorMessage.NUMBER_MUST_BE_BETWEEN,
                        BullsAndCowsCode.MIN_POSSIBLE_CHARS,
                        BullsAndCowsCode.MAX_POSSIBLE_CHARS
                    )
                )
            );
        }

        return infoInt;
    }

    public void generateRandomCode() throws InputMismatchException {
        int length = inputCodeInfo(Message.ASK_CODE_LENGTH);
        int possibleCharsAmount = inputCodeInfo(Message.ASK_POSSIBLE_CHARS_AMOUNT);

        try {
            this.game.setCode(
                BullsAndCowsCode.generateRandomCode(
                    length,
                    possibleCharsAmount
                )
            );
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new InputMismatchException(
                String.format(
                    ErrorMessage.ERROR,
                    String.format(
                        ErrorMessage.NUMBER_MUST_BE_BETWEEN,
                        length,
                        BullsAndCowsCode.MAX_POSSIBLE_CHARS
                    )
                )
            );
        }

        System.out.printf(
            Message.CODE_PREPARED,
            Message.CENSOR_CHAR.repeat(length),
            this.game.getCode().getPossibleCharsString()
        );
    }

    private boolean makeTurnWithResult(int turnNumber) {
        BullsAndCowsCode guessedCode;
        int[] bullsAndCows;

        System.out.printf(Message.TURN_NUMBER, turnNumber);
        for (;;) {
            String codeString;
            codeString = inputMaster.nextLine();

            boolean isIllegal = false;
            if (codeString.length() != this.game.getCode().getCapacity()) {
                System.out.printf(
                    ErrorMessage.WARNING,
                    ErrorMessage.CODE_CANNOT_BE_ACCEPTED
                );

                System.out.printf(
                    ErrorMessage.CODE_LENGTH_MUST_BE,
                    this.game.getCode().getCapacity()
                );

                isIllegal = true;
            }

            for (char codeCh: codeString.toCharArray()) {
                if (!this.game.getCode().checkCharPossibility(codeCh)) {
                    if (!isIllegal) {
                        System.out.printf(
                            ErrorMessage.WARNING,
                            ErrorMessage.CODE_CANNOT_BE_ACCEPTED
                        );
                    }

                    System.out.printf(
                        ErrorMessage.CODE_CONTAINS_ILLEGAL_CHAR,
                        codeCh
                    );

                    isIllegal = true;
                }
            }

            if (isIllegal) {

                continue;
            }

            guessedCode = new BullsAndCowsCode(codeString, this.game.getCode().getPossibleCharsAmount());
            break;
        }

        bullsAndCows = this.game.getCode().compareInBullsAndCows(guessedCode);
        System.out.print(Message.gradeBuilder(bullsAndCows[0], bullsAndCows[1]));

        return bullsAndCows[0] == this.game.getCode().getCapacity();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void executeGame() {
        System.out.print(Message.LETS_START);
        for (int i = 1; !makeTurnWithResult(i); ++i) {}
        System.out.print(Message.CONGRATULATIONS);
    }
}
