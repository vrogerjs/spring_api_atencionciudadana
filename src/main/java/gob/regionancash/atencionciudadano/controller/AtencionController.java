package gob.regionancash.atencionciudadano.controller;

import gob.regionancash.atencionciudadano.exception.ResourceNotFoundException;
import gob.regionancash.atencionciudadano.model.*;
import gob.regionancash.atencionciudadano.repository.AtencionRepository;
import io.lettuce.core.ScriptOutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import gob.regionancash.atencionciudadano.service.ContractService;

@RestController
@RequestMapping("/atencion")
public class AtencionController {

    @Autowired
    private AtencionRepository atencionRepository;

    @Autowired
    private ContractService contractService;

    @GetMapping("/{from}/{to}")
    //@PreAuthorize("hasAuthority('REGISTER_ATENCION_CIUDADANA')")
    public Page getAllAtenciones(Authentication authentication, @PathVariable(value = "from") int from,
                                 @PathVariable(value = "to") int to,
                                 @RequestParam(name = "activo", required = false, defaultValue = "1") Integer activo,
                                 @RequestParam(name = "dependencia", required = false) Long dependenciaId,
                                 @RequestParam(name = "fecha", required = false) LocalDate fecha

    ) throws Exception {
        User userDetails = (User) authentication.getPrincipal();
        Integer directory = userDetails.getDirectory();
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.DESC, "id"));

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN_ATENCION_CIUDADANA"))) {
            return atencionRepository.findAllByDependencia(PageRequest.of(from, to, sort), activo, dependenciaId, fecha);
        } else {
            List<Map> contracts = contractService.getContracts(directory);
            if (dependenciaId == null || !contracts.stream().anyMatch((c) -> {
                return dependenciaId == Long.parseLong(c.get("dependencyId").toString());
            })) {
                return Page.empty();
            }
            return atencionRepository.findAllByDependencia(PageRequest.of(from, to, sort), activo, dependenciaId, fecha);
        }
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
    public List<Atencion> getAtencionesBydependenciaId(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
        List<Atencion> l = atencionRepository.findByDependenciaId(id);
        return l;
    }

    @PostMapping("")
    public Atencion createAtencion(@Valid @RequestBody Atencion atencion) {
        return atencionRepository.save(atencion);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('REGISTER_ATENCION_CIUDADANA')")
    public ResponseEntity<Atencion> updateAtencion(@PathVariable(value = "id") Long atencionId, @Valid @RequestBody Atencion atencionData) throws ResourceNotFoundException {
        Atencion atencion = atencionRepository.findById(atencionId).orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada: " + atencionId));

        if (atencionData.getNroatencion() != null)
            atencion.setNroatencion(atencionData.getNroatencion());
        if (atencionData.getNroExpediente() != null)
            atencion.setNroExpediente(atencionData.getNroExpediente());
        if (atencionData.getMotivo() != null)
            atencion.setMotivo(atencionData.getMotivo());
        if (atencionData.getHoraIni() != null)
            atencion.setHoraIni(atencionData.getHoraIni());
        if (atencionData.getHoraFin() != null)
            atencion.setHoraFin(atencionData.getHoraFin());
        if (atencionData.getFecha() != null)
            atencion.setFecha(atencionData.getFecha());

        if (atencionData.getDependencia() != null)
            atencion.setDependencia(atencionData.getDependencia());

        if (atencionData.getPersona() != null)
            atencion.setPersona(atencionData.getPersona());

        if (atencionData.getActivo() != null)
            atencion.setActivo(atencionData.getActivo());

        atencion.setUpdatedAt(new Date());
        Atencion updatedAtencion = atencionRepository.save(atencion);
        return ResponseEntity.ok(updatedAtencion);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('REGISTER_ATENCION_CIUDADANA')")
    public Map<String, Boolean> deleteAtencion(@PathVariable(value = "id") Long atencionId) throws Exception {
        Atencion atencion = atencionRepository.findById(atencionId).orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada: " + atencionId));

        atencionRepository.delete(atencion);
        Map<String, Boolean> response = new HashMap<>();
        response.put("message", Boolean.TRUE);
        return response;
    }
}
