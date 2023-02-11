package gob.regionancash.atencionciudadano.controller;

import gob.regionancash.atencionciudadano.exception.ResourceNotFoundException;
import gob.regionancash.atencionciudadano.model.*;
import gob.regionancash.atencionciudadano.repository.AtencionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/atencion")
public class AtencionController {

    @Autowired
    private AtencionRepository atencionRepository;

    @GetMapping("/{from}/{to}")
    public Page getAllAtenciones(@PathVariable(value = "from") int from, @PathVariable(value = "to") int to,
                                 @RequestParam(name = "dependencia", required = false) Long IdDependencia, @RequestParam(name = "activo") Integer activo
    ) throws Exception {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "id"));
        var pageable = PageRequest.of(from, to);
        return atencionRepository.findAllByDependencia(pageable, IdDependencia, activo);
    }

    @GetMapping("/demo/{from}/{to}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page getAllAtenciones(Authentication authentication, @PathVariable(value = "from") int from, @PathVariable(value = "to") int to, @RequestParam(name = "dependencia", required = false) Long IdDependencia, @RequestParam(name = "activo") Integer activo
    ) throws Exception {

        User userDetails = (User) authentication.getPrincipal();
        userDetails.getDirectory();
//        userDetails.getAuthorities().contains();
        System.out.println(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("GRAND_1")));

        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "id"));
        var pageable = PageRequest.of(from, to);
        return atencionRepository.findAllByDependencia(pageable, IdDependencia, activo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atencion> getAtencionesById(@PathVariable(value = "id") Long atencionId) throws ResourceNotFoundException {
        Atencion atencion = atencionRepository.findById(atencionId).orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada :" + atencionId));
        return ResponseEntity.ok().body(atencion);
    }

    @GetMapping("/search/{id}")
    public List<Atencion> getAtencionesByIdPersona(@PathVariable(value = "id") String id) throws ResourceNotFoundException {
        List<Atencion> l = atencionRepository.findByPersonaNroDocumento(id);
        return l;
    }

    @GetMapping("/search/dependencia/{id}")
    public List<Atencion> getAtencionesByIdDependencia(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
        List<Atencion> l = atencionRepository.findByDependenciaId(id);
        return l;
    }

    @PostMapping("")
    public Atencion createAtencion(@Valid @RequestBody Atencion atencion) {
        System.out.println(atencion.getDependencia());
        return atencionRepository.save(atencion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atencion> updateAtencion(@PathVariable(value = "id") Long atencionId, @Valid @RequestBody Atencion atencionDetalles) throws ResourceNotFoundException {
        Atencion atencion = atencionRepository.findById(atencionId).orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada: " + atencionId));

        if (atencionDetalles.getNroatencion() != null)
            atencion.setNroatencion(atencionDetalles.getNroatencion());

        //if (atencionDetalles.getNrosisgedo() != null)
        //    atencion.setNrosisgedo(atencionDetalles.getNrosisgedo());

        if (atencionDetalles.getNroexpediente() != null)
            atencion.setNroexpediente(atencionDetalles.getNroexpediente());

        if (atencionDetalles.getMotivo() != null)
            atencion.setMotivo(atencionDetalles.getMotivo());

        if (atencionDetalles.getHoraini() != null)
            atencion.setHoraini(atencionDetalles.getHoraini());

        if (atencionDetalles.getHorafin() != null)
            atencion.setHorafin(atencionDetalles.getHorafin());

        if (atencionDetalles.getFecha() != null)
            atencion.setFecha(atencionDetalles.getFecha());

        if (atencionDetalles.getDependencia() != null)
            atencion.setDependencia(atencionDetalles.getDependencia());

        if (atencionDetalles.getPersona() != null)
            atencion.setPersona(atencionDetalles.getPersona());

        if (atencionDetalles.getActivo() != null)
            atencion.setActivo(atencionDetalles.getActivo());

        atencion.setUpdatedAt(new Date());
        Atencion updatedAtencion = atencionRepository.save(atencion);
        return ResponseEntity.ok(updatedAtencion);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteAtencion(@PathVariable(value = "id") Long atencionId) throws Exception {
        Atencion atencion = atencionRepository.findById(atencionId).orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada: " + atencionId));

        atencionRepository.delete(atencion);
        Map<String, Boolean> response = new HashMap<>();
        response.put("message", Boolean.TRUE);
        return response;
    }
}
