package gameAdmin;

public enum PrizeMoney {
    TEN_THOUSAND(10000),
    TWENTY_THOUSAND(20000),
    FORTY_THOUSAND(40000),
    EIGHTY_THOUSAND(80000),
    ONE_LAKH(100000),
    TWO_LAKH(200000),
    FOUR_LAKH(400000),
    EIGHT_LAKH(800000),
    SIXTEEN_LAKH(1600000),
    THIRTY_TWO_LAKH(3200000),
    SIXTY_FOUR_LAKH(6400000),
    ONE_CRORE(10000000);

    private final int amount;

    PrizeMoney(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public static PrizeMoney getByIndex(int index) {
        PrizeMoney[] values = PrizeMoney.values();
        if (index >= 0 && index < values.length) {
            return values[index];
        } else {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
    }
}
