
// Абстрактный класс обработчика заявок
public abstract class CreditHandler {
    private CreditHandler nextHandler;

    public CreditHandler setNextHandler(CreditHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public void handle(CreditRequest request) {
        if (canHandle(request)) {
            process(request);
        }

        if (nextHandler != null) {
            // Если это не RejectionHandler - передаем дальше всегда
            // Если это RejectionHandler - передаем только если есть причина отказа
            if (!(nextHandler instanceof RejectionHandler) || request.getRejectionReason() != null) {
                nextHandler.handle(request);
            }
        }
    }
    protected abstract boolean canHandle(CreditRequest request);

    protected abstract void process(CreditRequest request);
}
