
// Класс заявки на кредит
public class CreditRequest {
    private final String clientName; //имя клиента
    private final int creditScore; //кредитный рейтинг
    private final double requestedAmount; //запрашиваемая сумма
    private final double income; //годовой доход
    private boolean approved; //принята или отклонена
    private double approvedAmount; //сумма, которая была одобрена
    private String rejectionReason; //причина отказа

    public CreditRequest(String clientName, int creditScore, double income, double requestedAmount) {
        this.clientName = clientName;
        this.creditScore = creditScore;
        this.income = income;
        this.requestedAmount = requestedAmount;
    }

    public String getClientName() {
        return clientName;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getIncome() {
        return income;
    }

    public double getApprovedAmount() {
        return approvedAmount;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setApprovedAmount(double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
