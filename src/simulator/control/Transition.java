package simulator.control;

public class Transition {
    
    private final String from;
    private final String symbol;
    private final String to;
    
    public Transition(String from, String symbol, String to) {
        this.from = from;
        this.symbol = symbol;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTo() {
        return to;
    }
}
