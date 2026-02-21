package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer(String name, BigDecimal oldBalance, BigDecimal newBalance) {

  public TransactionCustomer {
    Objects.requireNonNull(name);
    Objects.requireNonNull(oldBalance);
    Objects.requireNonNull(newBalance);

    if (oldBalance.signum() < 0) throw new IllegalArgumentException("O valor de oldBalance deve ser positivo ou zero: " + oldBalance);
    if (newBalance.signum() < 0) throw new IllegalArgumentException("O valor de newBalance deve ser positivo ou zero: " + newBalance);
    if (name.trim().isEmpty()) throw new IllegalArgumentException("O name nao pode ser vazio");
  }

}
