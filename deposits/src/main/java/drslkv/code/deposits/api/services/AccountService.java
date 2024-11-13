package drslkv.code.deposits.api.services;

import drslkv.code.deposits.api.dto.AccountDTO;
import drslkv.code.deposits.store.entities.AccountEntity;
import drslkv.code.deposits.store.repositories.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountEntity openAccount(String accountNumber, double balance) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber(accountNumber);
        accountEntity.setBalance(balance);
        accountEntity.setCurrency("RUB");
        return accountRepository.save(accountEntity);
    }

    public AccountEntity deposit(String accountNumber, double amount) {
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity == null) {
            throw new EntityNotFoundException("Счет с номером " + accountNumber + " не найден.");
        }
        accountEntity.setBalance(accountEntity.getBalance() + amount);
        return accountRepository.save(accountEntity);
    }

    public AccountEntity withdraw(String accountNumber, double amount) {
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity == null) {
            throw new EntityNotFoundException("Счет с номером " + accountNumber + " не найден.");
        }
        if (accountEntity.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств на счете " + accountNumber
                    + " для выполнения операции.");
        }
        accountEntity.setBalance(accountEntity.getBalance() - amount);
        return accountRepository.save(accountEntity);
    }

    public AccountEntity getAccountDetails(String accountNumber) {
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity == null) {
            throw new EntityNotFoundException("Счет с номером " + accountNumber + " не найден.");
        }
        return accountEntity;
    }

    public AccountDTO convertToDTO(AccountEntity accountEntity) {
        return new AccountDTO(
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getCurrency()
        );
    }
}
