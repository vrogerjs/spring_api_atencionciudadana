package gob.regionancash.atencionciudadano.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "atencions")
@EntityListeners(AuditingEntityListener.class)
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 16)
    private String nroatencion;

    //@Column(nullable = false, length = 16)
    //private String nrosisgedo;

    @Column(nullable = false, length = 16)
    private String nroExpediente;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime horaIni;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(columnDefinition = "TIME")
    private LocalTime horaFin;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(columnDefinition = "TIME")
    private LocalTime horaCancelar;

    @Column(columnDefinition = "TEXT")
    private String motivoCancelar;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate fecha;


    @Column(length = 8)
    private String nroDocumento1;

    @Column(length = 512)
    private String apellidoNombre1;

    @Column(length = 8)
    private String nroDocumento2;

    @Column(length = 512)
    private String apellidoNombre2;

    @Column(length = 8)
    private String nroDocumento3;

    @Column(length = 512)
    private String apellidoNombre3;

    @ManyToOne
    @JoinColumn(name="dependencia_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    private Dependencia dependencia;

    @ManyToOne
    @JoinColumn(name="persona_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    private Persona persona;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private Integer activo=1;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private Integer borrado=0;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


}
