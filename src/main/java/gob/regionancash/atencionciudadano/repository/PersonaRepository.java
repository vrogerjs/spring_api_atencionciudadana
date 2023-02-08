package gob.regionancash.atencionciudadano.repository;

import gob.regionancash.atencionciudadano.model.Cronograma;
import gob.regionancash.atencionciudadano.model.Dependencia;
import gob.regionancash.atencionciudadano.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    public abstract Optional<Persona> findByNroDocumento(String dependencia);

    public abstract Persona getByNroDocumento(String dependencia);
}
