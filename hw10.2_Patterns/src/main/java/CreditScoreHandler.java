
//класс обработчика кредитного рейтинга
public class CreditScoreHandler extends CreditHandler{
    @Override
    protected boolean canHandle(CreditRequest request) {
        return true;
    }

    @Override
    protected void process(CreditRequest request) {
        if (request.getCreditScore() < 1000) {
            request.setApproved(false);
            request.setRejectionReason("Кредитный рейтинг слишком низкий");
        } else {
            request.setApproved(true);
        }
    }
}
