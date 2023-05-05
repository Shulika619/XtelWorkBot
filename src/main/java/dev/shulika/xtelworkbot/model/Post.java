package dev.shulika.xtelworkbot.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Post")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_employee_id")
    private Employee fromEmployee;
    @ManyToOne
    @JoinColumn(name = "to_department_id")
    private Department toDepartment;
    @ManyToOne
    @JoinColumn(name = "accepted_employee_id")
    private Employee taskExecutor;
    private String textMsg;

//    @Temporal(TemporalType.DATE)
//    @CreationTimestamp
//    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    private String fileId;
}