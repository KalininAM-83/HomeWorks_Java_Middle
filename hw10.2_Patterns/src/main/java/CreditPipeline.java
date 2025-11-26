import java.util.ArrayList;
import java.util.List;

//класс кредитного конвейера
public class CreditPipeline {
    private final List<CreditHandler> handlers = new ArrayList<>();

    //цепочка обработчиков
    public CreditPipeline() {
        var scoreHandler = new CreditScoreHandler();
        var incomeHandler = new IncomeHandler();
        var finalHandler = new FinalApprovalHandler();
        var rejectHandler = new RejectionHandler();

        //устанавливаем цепочку
        scoreHandler.setNextHandler(incomeHandler)
                .setNextHandler(finalHandler)
                .setNextHandler(rejectHandler);

        handlers.add(scoreHandler);
        handlers.add(incomeHandler);
        handlers.add(finalHandler);
        handlers.add(rejectHandler);
    }

    public void processRequest(CreditRequest request) {
        if (!handlers.isEmpty()) {
            handlers.getFirst().handle(request);
        }
    }
}
