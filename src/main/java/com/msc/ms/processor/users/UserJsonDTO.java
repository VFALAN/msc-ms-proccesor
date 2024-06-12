package com.msc.ms.processor.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserJsonDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("apellido_materno")
    private String apellidoMaterno;
    @JsonProperty("apellido_paterno")
    private String apellidoPaterno;
    @JsonProperty("curp")
    private String curp;
    @JsonProperty("fecha_nacimiento")
    private Date fechaNacimiento;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("correo")
    private String correo;
    @JsonProperty("edad")
    private Integer edad;
    @JsonProperty("genero")
    private String genero;
    @JsonProperty("telefono")
    private String telefono;
    @JsonProperty("localidad_id")
    private Integer localidadId;
}
