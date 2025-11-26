import static java.lang.System.out;

//класс обработчика отказа
public class RejectionHandler extends CreditHandler{
    @Override
    protected boolean canHandle(CreditRequest request) {
        return !request.isApproved() && request.getRejectionReason() != null;
    }

    @Override
    protected void process(CreditRequest request) {
        out.println("Кредитная заявка отклонена для клиента: " + request.getClientName());
        out.println("Причина отказа: " + request.getRejectionReason());
    }
}
