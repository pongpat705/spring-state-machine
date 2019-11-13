package com.maoz.app;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="book_flow")
@Data
public class BookFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ticketId;
    private String state;
    private Date createdDate;
}
