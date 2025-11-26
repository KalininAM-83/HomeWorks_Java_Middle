import static java.lang.System.out;

//класс обработчика финального одобрения
public class FinalApprovalHandler extends CreditHandler{
    @Override
    protected boolean canHandle(CreditRequest request) {
        return request.isApproved();
    }

    @Override
    protected void process(CreditRequest request) {
        out.println("Кредитная заявка одобрена для клиента: " + request.getClientName());
        out.println("Одобренная сумма: " + request.getApprovedAmount());
    }
}
