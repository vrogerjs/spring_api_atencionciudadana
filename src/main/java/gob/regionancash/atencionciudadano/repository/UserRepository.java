package gob.regionancash.atencionciudadano.repository;

import gob.regionancash.atencionciudadano.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
