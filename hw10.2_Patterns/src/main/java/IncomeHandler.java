
//класс обработчика дохода
public class IncomeHandler extends CreditHandler{
    @Override
    protected boolean canHandle(CreditRequest request) {
        return request.isApproved() || request.getRejectionReason() == null;
    }

    @Override
    protected void process(CreditRequest request) {
        double maxAllowedAmount = request.getIncome() * 0.5; //макимальная разрешенная сумма - 50% от годового дохода
        if (request.getRequestedAmount() > maxAllowedAmount) {
            request.setApproved(false);
            request.setRejectionReason("Запрошенная сумма превышает допустимый лимит");
        } else {
            request.setApproved(true);
            request.setApprovedAmount(request.getRequestedAmount());
        }
    }
}
