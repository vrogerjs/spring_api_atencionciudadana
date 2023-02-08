package gob.regionancash.atencionciudadano.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "dependencias")
@EntityListeners(AuditingEntityListener.class)
public class Dependencia {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256, unique = true)
    private String dependencia;

    @Column(nullable = true, length = 32)
    private String abreviatura;

    @Column(nullable = true, length = 256)
    private String nombaperesponsable;

    @Column(nullable = true, length = 256)
    private String cargoresponsable;

    @Column(nullable = false, length = 1)
    private Integer activo=1;

    @Column(nullable = false, length = 1)
    private Integer borrado=0;

    @JsonIgnore
    @OneToMany(mappedBy="dependencia")
    private Set<Cronograma> cronogramas;

    @JsonIgnore
    @OneToMany(mappedBy="dependencia")
    private Set<Atencion> atencions;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

}
