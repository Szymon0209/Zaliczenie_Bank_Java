public class Transfer {
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private double amount;
    private boolean isVerified;

    public Transfer(String senderAccountNumber, String recipientAccountNumber, double amount) {
        this.senderAccountNumber = senderAccountNumber;
        this.recipientAccountNumber = recipientAccountNumber;
        this.amount = amount;
        this.isVerified = false;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}