package de.eimantas.eimantasbackend.repo;

import de.eimantas.eimantasbackend.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;


public interface AccountRepository extends CrudRepository<Account, Long> {

    public Stream<Account> findByUserId(String userId);

}