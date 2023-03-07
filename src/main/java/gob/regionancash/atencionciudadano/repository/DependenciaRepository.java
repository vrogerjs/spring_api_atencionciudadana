package gob.regionancash.atencionciudadano.repository;

import gob.regionancash.atencionciudadano.model.Cronograma;
import gob.regionancash.atencionciudadano.model.Dependencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DependenciaRepository extends JpaRepository<Dependencia, Long> {

    public abstract Dependencia getByName(String dependencia);

}
