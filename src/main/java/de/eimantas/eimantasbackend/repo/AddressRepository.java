package de.eimantas.eimantasbackend.repo;

import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.AddressProject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public interface AddressRepository extends CrudRepository<AddressProject, Long> {

  Stream<AddressProject> findByUserId(String userId);

  Optional<AddressProject> findByIdAndUserId(long accountId, String userIdFromPrincipal);

}