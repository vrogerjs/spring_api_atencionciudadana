package gob.regionancash.atencionciudadano.controller;

import gob.regionancash.atencionciudadano.exception.ResourceNotFoundException;
import gob.regionancash.atencionciudadano.model.Dependencia;
import gob.regionancash.atencionciudadano.repository.DependenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/dependencia")
public class DependenciaController {

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @GetMapping("")
    public List<Dependencia> getAllDependencias() throws Exception {
        return dependenciaRepository.findAll();
    }

    @GetMapping("/{from}/{to}")
    public Page getAllAtenciones(@PathVariable(value = "from") int from, @PathVariable(value = "to") int to) throws Exception {
        HashMap m=new HashMap();
        Sort sort =  Sort.by(new Sort.Order(Sort.Direction.ASC, "id"));
        var pageable= PageRequest.of(from, to);
        return dependenciaRepository.findAll( pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dependencia> getDependenciasById(@PathVariable(value = "id") Long dependenciaId) throws ResourceNotFoundException {
        Dependencia dependencia = dependenciaRepository.findById(dependenciaId).orElseThrow(() -> new ResourceNotFoundException("Dependencia no encontrada :" + dependenciaId));
        return ResponseEntity.ok().body(dependencia);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<Dependencia> getByDependencia(@PathVariable(value = "name") String dependenciaName) throws ResourceNotFoundException {
        Dependencia dependencia = dependenciaRepository.getByName(dependenciaName);
        return ResponseEntity.ok().body(dependencia);
    }

    @PostMapping("")
    public Dependencia createDependencia(@Valid @RequestBody Dependencia dependencia) {
        return dependenciaRepository.save(dependencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dependencia> updateDependencia(@PathVariable(value = "id") Long dependenciaId, @Valid @RequestBody Dependencia dependenciaData) throws ResourceNotFoundException {

        Dependencia dependencia = dependenciaRepository.findById(dependenciaId).orElseThrow(() -> new ResourceNotFoundException("Dependencia no encontrada: " + dependenciaId));

        if (dependenciaData.getName() != null)
            dependencia.setName(dependenciaData.getName());

        if (dependenciaData.getAbreviatura() != null)
            dependencia.setAbreviatura(dependenciaData.getAbreviatura());

        if (dependenciaData.getNApellidoNombreresponsable() != null)
            dependencia.setNApellidoNombreresponsable(dependenciaData.getNApellidoNombreresponsable());

        if (dependenciaData.getCargoresponsable() != null)
            dependencia.setCargoresponsable(dependenciaData.getCargoresponsable());

        dependencia.setUpdatedAt(new Date());
        Dependencia updatedDependencia = dependenciaRepository.save(dependencia);
        return ResponseEntity.ok(updatedDependencia);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteDependencia(@PathVariable(value = "id") Long dependenciaId) throws Exception {
        Dependencia dependencia = dependenciaRepository.findById(dependenciaId).orElseThrow(() -> new ResourceNotFoundException("Dependencia no encontrada: " + dependenciaId));

        dependenciaRepository.delete(dependencia);
        Map<String, Boolean> response = new HashMap<>();
        response.put("message", Boolean.TRUE);
        return response;
    }
}
