package com.joboffers.domain.offer;

import com.joboffers.domain.loginandregister.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Offer {

    @Id
    private Long id;
    private String title;
    private String description;
    private String company;
    private LocalDateTime publicationDate;
    private boolean isActive;
    private List<User> usersApplyingForJob;

}
