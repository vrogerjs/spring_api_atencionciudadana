package gob.regionancash.atencionciudadano.controller;

import gob.regionancash.atencionciudadano.exception.ResourceNotFoundException;
import gob.regionancash.atencionciudadano.model.Cronograma;
import gob.regionancash.atencionciudadano.model.Dependencia;
import gob.regionancash.atencionciudadano.repository.AtencionRepository;
import gob.regionancash.atencionciudadano.repository.CronogramaRepository;
import gob.regionancash.atencionciudadano.repository.DependenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cronograma")
public class CronogramaController {


    @Autowired
    private CronogramaRepository cronogramaRepository;

    @Autowired
    private DependenciaRepository dependenciaRepository;

    @Autowired
    private AtencionRepository atencionRepository;

    @GetMapping("")
    public List<Cronograma> getAllCronogramas() throws Exception {
        return cronogramaRepository.findAll();
    }

    @GetMapping("/dependencia/{id}")
    public List<Cronograma> getCronogramaById(@PathVariable(value = "id") Long dependenciaId) {
        return cronogramaRepository.findByIdDependencia(dependenciaId);

    }

    @GetMapping("/fechaDisponible/{name}")
    public Object getFechaDisponible(@PathVariable(value = "name") String dependenciaName, @RequestParam(name = "dia") Integer dia) throws Exception {

        Dependencia d = dependenciaRepository.getByName(dependenciaName);

        Calendar c = Calendar.getInstance();

        List<Cronograma> l = cronogramaRepository.findByDependenciaAndDia(d.getId(), dia);
        List l2 = new ArrayList();
        Cronograma cronograma = l.get(0);

        //      return l;

        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

        c.set(Calendar.DAY_OF_WEEK, cronograma.getDia());
        c.add(Calendar.DAY_OF_WEEK, 0);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;
        for (int i = 0; i < 4; i++) {

            //////ini set times
            c.set(Calendar.HOUR_OF_DAY, cronograma.getHoraIni().getHour());
            c.set(Calendar.MINUTE, cronograma.getHoraIni().getMinute());
            c.set(Calendar.SECOND, cronograma.getHoraIni().getSecond());
            List<Object[]> times = new ArrayList();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            long max = cronograma.getHoraFin().getHour() * 10000 + cronograma.getHoraFin().getMinute() * 100 + cronograma.getHoraFin().getSecond();
            long max2 = cronograma.getHoraIni().getHour() * 10000 + cronograma.getHoraIni().getMinute() * 100 + cronograma.getHoraIni().getSecond();

            long rest = max - max2;
            long mult = rest * 60;
            long div = (mult / 200000);

            long cur = 0;
            do {
                times.add(new Object[]{format.format(c.getTime()), null});
                c.add(Calendar.MINUTE, (int) div);

                cur = c.get(Calendar.HOUR_OF_DAY) * 10000 +
                        c.get(Calendar.MINUTE) * 100 +
                        c.get(Calendar.SECOND);
            } while (cur < max);
            //end set times

            List<Object[]> c2 = atencionRepository.getCountByDependenciaAndDateAndFechaIni(d, c.getTime());

            times = times.stream().map(row -> {
                for (Object[] r2 : c2) {
                    if (row[0].equals(((LocalTime) r2[1]).format(formatter))) {
                        row[1] = 1;
                    }
                }
                return row;
            }).collect(Collectors.toList());

            l2.add(new Object[]{c.getTime(), times, c2});

            c.add(Calendar.DAY_OF_MONTH, 7);
        }
        HashMap m = new HashMap();
        m.put("times", l2);
        m.put("dependency", d.getId());

        return m;

    }

    @GetMapping("/{from}/{to}")
    public Page getAllCronograma(@PathVariable(value = "from") int from, @PathVariable(value = "to") int to,
                                 @RequestParam(name = "dependencia", required = false) Long IdDependencia) throws Exception {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "id"));
        var pageable = PageRequest.of(from, to);
        return cronogramaRepository.findAllByDependencia(pageable, IdDependencia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cronograma> getCronogramasById(@PathVariable(value = "id") Long cronogramaId) throws ResourceNotFoundException {
        Cronograma cronograma = cronogramaRepository.findById(cronogramaId).orElseThrow(() -> new ResourceNotFoundException("Cronograma no encontrada :" + cronogramaId));
        return ResponseEntity.ok().body(cronograma);
    }

    @PostMapping("")
    public Cronograma createCronograma(@Valid @RequestBody Cronograma cronograma) {
        Dependencia dependencia = cronograma.getDependencia();
        if (!dependenciaRepository.findById(dependencia.getId()).isPresent()) {
            dependenciaRepository.save(dependencia);
        }
        return cronogramaRepository.save(cronograma);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cronograma> updateCronograma(@PathVariable(value = "id") Long cronogramaId, @Valid @RequestBody Cronograma cronogramaDetalles) throws ResourceNotFoundException {

        Cronograma cronograma = cronogramaRepository.findById(cronogramaId).orElseThrow(() -> new ResourceNotFoundException("Cronograma no encontrada: " + cronogramaId));

        if (cronogramaDetalles.getDia() != null)
            cronograma.setDia(cronogramaDetalles.getDia());

        if (cronogramaDetalles.getHoraIni() != null)
            cronograma.setHoraIni(cronogramaDetalles.getHoraIni());

        if (cronogramaDetalles.getHoraFin() != null)
            cronograma.setHoraFin(cronogramaDetalles.getHoraFin());

        if (cronogramaDetalles.getActivo() != null)
            cronograma.setActivo(cronogramaDetalles.getActivo());

        if (cronogramaDetalles.getDependencia() != null)
            cronograma.setDependencia(cronogramaDetalles.getDependencia());

        cronograma.setUpdatedAt(new Date());
        Cronograma updatedCronograma = cronogramaRepository.save(cronograma);
        return ResponseEntity.ok(updatedCronograma);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteCronograma(@PathVariable(value = "id") Long cronogramaId) throws Exception {
        Cronograma cronograma = cronogramaRepository.findById(cronogramaId).orElseThrow(() -> new ResourceNotFoundException("Cronograma no encontrada: " + cronogramaId));

        cronogramaRepository.delete(cronograma);
        Map<String, Boolean> response = new HashMap<>();
        response.put("message", Boolean.TRUE);
        return response;
    }
}
