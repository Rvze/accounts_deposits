package drslkv.code.deposits.store.repositories;

import drslkv.code.deposits.store.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    AccountEntity findByAccountNumber(String accountNumber);
}
