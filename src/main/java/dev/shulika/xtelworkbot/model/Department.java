package dev.shulika.xtelworkbot.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Department")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;

    @OneToMany(mappedBy="idDepartment")
    private List<Employee> employee;
}