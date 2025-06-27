public enum Shift {
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    EVENING("Evening");
    
    private final String displayName;
    
    Shift(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 