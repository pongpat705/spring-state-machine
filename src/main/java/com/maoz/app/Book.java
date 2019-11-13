package com.maoz.app;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    private String ticketId;
    private String state;
    private String event;
    @Lob
    private String data;
    private Date createdDate;
}
