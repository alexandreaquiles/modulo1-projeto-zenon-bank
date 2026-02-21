package br.com.zenon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Main {

  void main() {
    var t1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9838.64"),
        new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
        new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
        false, false
    );

    var t2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
        new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
        new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
        true, false
    );

    IO.println(t1);
    IO.println(t2);

    IO.println("--------------------------------------------------------------");


    var transactionIngestor = new TransactionIngestor();
    List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");
    IO.println(transactions.size());

    transactions.stream().limit(10).forEach(IO::println);

    IO.println("--------------------------------------------------------------");

    List<Transaction> transactionsBadData = transactionIngestor.read("data/paysim_with_bad_data.csv");
    IO.println(transactionsBadData.size());

    transactionsBadData.forEach(IO::println);

    //tentando criar objeto invalido
//    new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9838.64"),
//        null,
//        new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
//        false, false
//    );

    IO.println("--------------------------------------------------------------");

    var fraudAnalyzer = new FraudAnalyzer(transactions);

    //     Apenas transações onde isFraud == true, imprima o tamanho da lista.
    long fraudCount = fraudAnalyzer.countFrauds();
    IO.println("Total de fraudes: " + fraudCount);

    //     Imprima as 3 fraudes de maior valor (amount).
    List<BigDecimal> highestFraudAmounts = fraudAnalyzer.findHighestValueFraudAmounts(3);
    IO.println("Top 3 fraudes de maior valor:");
    highestFraudAmounts.forEach(amount -> IO.println("- %.2f".formatted(amount)));


    //     Obter apenas os nomes dos clientes de origem (nameOrig) dessas fraudes e depois gere uma lista sem repetições (Set ou distinct) com os 5 maiores clientes suspeitos.
    List<String> suspiciousClients = fraudAnalyzer.findTopSuspiciousClients(5);
    IO.println("Top 5 clientes suspeitos:");
    suspiciousClients.forEach(IO::println);

    //     Calcule o prejuízo total causado pelas fraudes (soma dos amount).
    BigDecimal totalFraudLoss = fraudAnalyzer.calculateTotalFraudLoss();
    IO.println("Prejuízo total: " + totalFraudLoss);

    // Conte quantas fraudes ocorreram por tipo de transação (CASH_OUT, TRANSFER, etc...)
    Map<TransactionType, Long> fraudCountByType = fraudAnalyzer.countFraudsByType();
    IO.println("Fraudes por tipo:");
    fraudCountByType.forEach((type, count) -> IO.println("- %s: %d".formatted(type, count)));

    IO.println("--------------------------------------------------------------");

    TransactionRepository transactionRepository;

    transactionRepository = new TransactionListRepository(transactions);
    String notFoundOriginName = "C12345";
    transactionRepository.findByOriginName(notFoundOriginName)
        .ifPresentOrElse(IO::println, () -> IO.println("Transacao nao encontrada para " + notFoundOriginName));

    String existingOriginName = "C1868032458";

    long startTimeList = System.nanoTime();
    transactionRepository.findByOriginName(existingOriginName)
        .ifPresentOrElse(IO::println, () -> IO.println("Transacao nao encontrada para " + existingOriginName));
    long endTimeList = System.nanoTime();
    IO.println("Tempo de busca - List (ms): " + (endTimeList - startTimeList) / 1_000_000.0);

    transactionRepository = new TransactionMapRepository(transactions);
    startTimeList = System.nanoTime();
    transactionRepository.findByOriginName(existingOriginName)
        .ifPresentOrElse(IO::println, () -> IO.println("Transacao nao encontrada para " + existingOriginName));
    endTimeList = System.nanoTime();
    IO.println("Tempo de busca - Map (ms): " + (endTimeList - startTimeList) / 1_000_000.0);

  }

}
