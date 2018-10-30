package de.eimantas.eimantasbackend.repo;

import de.eimantas.eimantasbackend.entities.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public interface AccountRepository extends CrudRepository<Account, Long> {

  Stream<Account> findByUserId(String userId);

  @Query("select acc.id from #{#entityName} acc where userId = :userId")
  List<Long> getAllIds(@Param("userId") String accountId);

  Optional<Account> findByIdAndUserId(long accountId, String userIdFromPrincipal);
}