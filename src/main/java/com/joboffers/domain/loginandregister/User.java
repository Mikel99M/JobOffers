package com.joboffers.domain.loginandregister;

import com.joboffers.domain.offer.Offer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class User {

    @Id
    private Long id;
    private String name;
    private String email;
    private String password;
    private List<Offer> offersAppliedFor;

}
