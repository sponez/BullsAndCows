package bullscows;

import java.util.Random;

public class BullsAndCowsCode {
    private enum CharStatus {
        BULL,
        COW,
        NONE
    }

    public final static int MIN_POSSIBLE_CHARS;
    public final static int MAX_POSSIBLE_CHARS;
    private final static char[] ALL_POSSIBLE_CHARS;
    private final int capacity;
    private final int possibleCharsAmount;
    private final String code;

    static {
        final int numbersOffset = -'0';
        final int lettersOffset = '9' - '0' - 'a' + 1;

        MIN_POSSIBLE_CHARS = 1;
        MAX_POSSIBLE_CHARS = 36;
        ALL_POSSIBLE_CHARS = new char[MAX_POSSIBLE_CHARS];

        for (char ch = '0'; ch <= '9'; ++ch) {
            ALL_POSSIBLE_CHARS[numbersOffset + ch] = ch;
        }

        for (char ch = 'a'; ch <= 'z'; ++ch) {
            ALL_POSSIBLE_CHARS[lettersOffset + ch] = ch;
        }
    }

    public BullsAndCowsCode(String code, int possibleCharsAmount) {
        if (possibleCharsAmount <= 0 || possibleCharsAmount > MAX_POSSIBLE_CHARS) {
            throw new IllegalArgumentException();
        }

        this.code = code;
        this.capacity = code.length();
        this.possibleCharsAmount = possibleCharsAmount;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getPossibleCharsAmount() {
        return this.possibleCharsAmount;
    }

    public String getPossibleCharsString() {
        final int numbersOffset = 0;
        final int lettersOffset = '9' - '0' + 1;

        StringBuilder possibleChars = new StringBuilder();

        if (possibleCharsAmount <= numbersOffset) {
            return possibleChars.toString();
        }

        possibleChars.append(ALL_POSSIBLE_CHARS[numbersOffset]);

        if (possibleCharsAmount == numbersOffset + 1) {
            return possibleChars.toString();
        }

        possibleChars.append('-');

        if (possibleCharsAmount <= lettersOffset) {
            return possibleChars.append(ALL_POSSIBLE_CHARS[possibleCharsAmount - 1]).toString();
        }

        possibleChars
            .append(ALL_POSSIBLE_CHARS[lettersOffset - 1])
            .append(", ")
            .append(ALL_POSSIBLE_CHARS[lettersOffset]);

        if (possibleCharsAmount == lettersOffset + 1) {
            return possibleChars.toString();
        }

        return possibleChars
            .append('-')
            .append(ALL_POSSIBLE_CHARS[possibleCharsAmount - 1])
            .toString();
    }

    public boolean checkCharPossibility(char ch) {
        final int numbersOffset = 0;
        final int lettersOffset = '9' - '0' + 1;

        if (
            ch < ALL_POSSIBLE_CHARS[numbersOffset] ||
                ch > ALL_POSSIBLE_CHARS[MAX_POSSIBLE_CHARS - 1] ||
                ch > ALL_POSSIBLE_CHARS[lettersOffset - 1] && ch < ALL_POSSIBLE_CHARS[lettersOffset]
        ) {
            return false;
        }

        return ch <= ALL_POSSIBLE_CHARS[this.possibleCharsAmount - 1];
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static BullsAndCowsCode generateRandomCode(
        int capacity,
        int possibleCharsAmount
    ) throws IllegalArgumentException {
        if (
            capacity == 0 ||
                possibleCharsAmount ==0 ||
                capacity > possibleCharsAmount
        ) {
            throw new IllegalArgumentException();
        }

        StringBuilder newCode = new StringBuilder();
        Random random = new Random();
        boolean[] occupiedChars = new boolean[possibleCharsAmount];

        for (int i = 0; i < capacity; ++i) {
            int randomCharIndex = random.nextInt(possibleCharsAmount);

            while (occupiedChars[randomCharIndex]) {
                randomCharIndex = (randomCharIndex + 1) % possibleCharsAmount;
            }

            newCode.append(ALL_POSSIBLE_CHARS[randomCharIndex]);
            occupiedChars[randomCharIndex] = true;
        }

        return new BullsAndCowsCode(newCode.toString(), possibleCharsAmount);
    }

    private CharStatus findCharStatus(char ch, int position) {
        for (int i = 0; i < this.capacity; ++i) {
            if (this.code.charAt(i) == ch) {
                return i == position ? CharStatus.BULL : CharStatus.COW;
            }
        }

        return CharStatus.NONE;
    }

    public int[] compareInBullsAndCows(BullsAndCowsCode anotherCode) {
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < this.capacity; ++i) {
            switch (this.findCharStatus(anotherCode.toString().charAt(i), i)) {
                case BULL -> ++bulls;
                case COW -> ++cows;
            }
        }

        return new int[] {
            bulls,
            cows
        };
    }
}
