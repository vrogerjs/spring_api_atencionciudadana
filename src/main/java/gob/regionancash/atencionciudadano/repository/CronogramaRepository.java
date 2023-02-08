package gob.regionancash.atencionciudadano.repository;

import gob.regionancash.atencionciudadano.model.Cronograma;
import gob.regionancash.atencionciudadano.model.Dependencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {

    public abstract List<Cronograma> findByDependencia(Dependencia dependencia);

    @Query("SELECT c FROM Cronograma c WHERE (:dependenciaId IS NULL OR c.dependencia.id =:dependenciaId)")
    Page findAllByDependencia(PageRequest pageable, Long dependenciaId);

   // public abstract List<Cronograma> findByIdDependencia(Dependencia dependencia);

}
