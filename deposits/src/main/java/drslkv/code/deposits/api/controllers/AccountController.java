package drslkv.code.deposits.api.controllers;

import drslkv.code.deposits.api.dto.AccountDTO;
import drslkv.code.deposits.api.services.AccountService;
import drslkv.code.deposits.store.entities.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/open")
    public ResponseEntity<AccountDTO> openAccount(
            @RequestParam String accountNumber,
            @RequestParam double balance) {

        AccountEntity savedAccount = accountService.openAccount(accountNumber, balance);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.convertToDTO(savedAccount));
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountDTO> deposit(
            @RequestParam String accountNumber,
            @RequestParam double amount) {

        AccountEntity updatedAccount = accountService.deposit(accountNumber, amount);
        return ResponseEntity.ok(accountService.convertToDTO(updatedAccount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountDTO> withdraw(
            @RequestParam String accountNumber,
            @RequestParam double amount) {

        AccountEntity updatedAccount = accountService.withdraw(accountNumber, amount);
        return ResponseEntity.ok(accountService.convertToDTO(updatedAccount));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccountDetails(@PathVariable String accountNumber) {
        AccountEntity accountEntity = accountService.getAccountDetails(accountNumber);
        return ResponseEntity.ok(accountService.convertToDTO(accountEntity));
    }
}
