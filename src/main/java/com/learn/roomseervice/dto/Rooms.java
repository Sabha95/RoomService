package com.learn.roomseervice.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Rooms")
public class Rooms {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_number")
    private Long roomNumber;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "bin_status")
    private String binStatus;
    @Column(name = "common_area_clean_status")
    private String commonAreaCleanStatus;
    @Column(name = "bin_Count")
    private Integer binCount;

}
