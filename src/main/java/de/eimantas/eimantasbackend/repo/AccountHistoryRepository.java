package de.eimantas.eimantasbackend.repo;

import de.eimantas.eimantasbackend.entities.AccountHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AccountHistoryRepository extends CrudRepository<AccountHistory, Long> {

  List<AccountHistory> findByUserId(String userId);

  List<AccountHistory> findByRefAccountIdAndUserId(long refAccountId, String accountId);

}