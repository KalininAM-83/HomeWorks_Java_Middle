import static java.lang.System.out;

public class CreditApplication {
    public static void main(String[] args) {
        var pipeline = new CreditPipeline();

        // Примеры кредитных заявок
        var request1 = new CreditRequest("Иван Петров", 1000, 1000000, 200000);
        var request2 = new CreditRequest("Петр Иванов", 800, 2000000, 100000);
        var request3 = new CreditRequest("Агафон Луговой", 1200, 1000000, 2000000);

        out.println("\n===Демонстрация работы паттерна Chain of Responsibility===\n");

        // Обработка заявок
        pipeline.processRequest(request1);
        out.println();
        pipeline.processRequest(request2);
        out.println();
        pipeline.processRequest(request3);
        out.println();
    }
}
