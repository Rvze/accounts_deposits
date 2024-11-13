package drslkv.code.deposits.api.controllers;

import drslkv.code.deposits.api.dto.AccountDTO;
import drslkv.code.deposits.store.entities.AccountEntity;
import drslkv.code.deposits.store.repositories.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountRepository accountRepository;

    @PostMapping("/open")
    public ResponseEntity<AccountDTO> openAccount(
            @RequestParam String accountNumber,
            @RequestParam double balance) {

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber(accountNumber);
        accountEntity.setBalance(balance);
        accountEntity.setCurrency("RUB");

        AccountEntity savedAccount = accountRepository.save(accountEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedAccount));
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountDTO> deposit(
            @RequestParam String accountNumber,
            @RequestParam double amount) {

        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity == null) {
            throw new EntityNotFoundException("Счет с номером " + accountNumber + " не найден.");
        }
        accountEntity.setBalance(accountEntity.getBalance() + amount);
        AccountEntity updatedAccount = accountRepository.save(accountEntity);

        return ResponseEntity.ok(convertToDTO(updatedAccount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountDTO> withdraw(
            @RequestParam String accountNumber,
            @RequestParam double amount) {

        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity == null) {
            throw new EntityNotFoundException("Счет с номером " + accountNumber + " не найден.");
        }
        if (accountEntity.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств на счете " + accountNumber
                    + " для выполнения операции.");
        }
        accountEntity.setBalance(accountEntity.getBalance() - amount);
        AccountEntity updatedAccount = accountRepository.save(accountEntity);

        return ResponseEntity.ok(convertToDTO(updatedAccount));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccountDetails(@PathVariable String accountNumber) {
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity == null) {
            throw new EntityNotFoundException("Счет с номером " + accountNumber + " не найден.");
        }
        return ResponseEntity.ok(convertToDTO(accountEntity));
    }

    private AccountDTO convertToDTO(AccountEntity accountEntity) {
        return new AccountDTO(
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getCurrency()
        );
    }
}
