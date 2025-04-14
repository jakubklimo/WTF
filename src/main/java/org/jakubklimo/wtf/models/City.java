package org.jakubklimo.wtf.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private int country_id;
}
