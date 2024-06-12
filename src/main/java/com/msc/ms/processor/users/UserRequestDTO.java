package com.msc.ms.processor.users;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {

    private String name;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String userName;
    private Integer age;
    private Date birthDate;
    private String email;
    private Integer idLocation;
    private String street;
    private String number;
    private String description;
    private Integer idProfile;
}
