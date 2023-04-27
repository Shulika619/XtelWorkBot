package dev.shulika.xtelworkbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Employee")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_department", referencedColumnName="id")
    private Department idDepartment;
    private String fullName;
    private String tgFirstName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
//    private boolean isActive;
}