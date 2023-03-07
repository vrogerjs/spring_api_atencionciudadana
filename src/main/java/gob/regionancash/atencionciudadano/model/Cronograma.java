package gob.regionancash.atencionciudadano.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "cronogramas")
@EntityListeners(AuditingEntityListener.class)
public class Cronograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private Integer dia;

    @Column(nullable = true)
    private String texto;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime horaIni;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime horaFin;

    @Column(nullable = false)
    private int limite;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private Integer activo=1;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private Integer borrado=0;

    @ManyToOne
    @JoinColumn(name="dependencia_id", nullable=false)
    private Dependencia dependencia;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;
}
