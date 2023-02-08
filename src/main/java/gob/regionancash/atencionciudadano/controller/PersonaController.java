package gob.regionancash.atencionciudadano.controller;

import gob.regionancash.atencionciudadano.exception.ResourceNotFoundException;
import gob.regionancash.atencionciudadano.model.Persona;
import gob.regionancash.atencionciudadano.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/persona")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping("")
    public List<Persona> getAllPersonas() throws Exception {
        return personaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersonasById(@PathVariable(value = "id") Long personaId) throws ResourceNotFoundException {
        Persona persona = personaRepository.findById(personaId).orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada :" + personaId));
        return ResponseEntity.ok().body(persona);
    }

    @GetMapping("/nrodoc/{id}")
    public ResponseEntity<Persona> getPersonasByNroDocumento(@PathVariable(value = "id") String personaId) throws ResourceNotFoundException {
        Persona persona = personaRepository.findByNroDocumento(personaId).orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada :" + personaId));
        return ResponseEntity.ok().body(persona);
    }

    @PostMapping("")
    public Persona createPersona(@Valid @RequestBody Persona persona) {
        return personaRepository.save(persona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> updatePersona(@PathVariable(value = "id") Long personaId, @Valid @RequestBody Persona personaDetalles) throws ResourceNotFoundException {

        Persona persona = personaRepository.findById(personaId).orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada: " + personaId));

        if (personaDetalles.getTipoPersona() != null)
            persona.setTipoPersona(personaDetalles.getTipoPersona());

        if (personaDetalles.getTipoDocumento() != null)
            persona.setTipoDocumento(personaDetalles.getTipoDocumento());

        if (personaDetalles.getNroDocumento() != null)
            persona.setNroDocumento(personaDetalles.getNroDocumento());

        if (personaDetalles.getNombape() != null)
            persona.setNombape(personaDetalles.getNombape());

        if (personaDetalles.getRazonsocial() != null)
            persona.setRazonsocial(personaDetalles.getRazonsocial());

        if (personaDetalles.getRepresentantelegal() != null)
            persona.setRepresentantelegal(personaDetalles.getRepresentantelegal());

        if (personaDetalles.getDireccion() != null)
            persona.setDireccion(personaDetalles.getDireccion());

        if (personaDetalles.getCelular() != null)
            persona.setCelular(personaDetalles.getCelular());

        persona.setUpdatedAt(new Date());
        Persona updatedPersona = personaRepository.save(persona);
        return ResponseEntity.ok(updatedPersona);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deletePersona(@PathVariable(value = "id") Long personaId) throws Exception {
        Persona persona = personaRepository.findById(personaId).orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada: " + personaId));

        personaRepository.delete(persona);
        Map<String, Boolean> response = new HashMap<>();
        response.put("message", Boolean.TRUE);
        return response;
    }
}
