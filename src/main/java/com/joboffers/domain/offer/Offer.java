package com.joboffers.domain.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Document(collection = "Offers")
public class Offer {

    @Id
    private String id;
    @Field("title")
    private String title;
    @Field("description")
    private String description;
    @Field("company")
    private String company;
    @Field
    private String salary;
    @Field("Date of publication")
    private Instant publicationDate;
    @Field("url")
    @Indexed(unique = true)
    private String offerUrl;
    @Field("is active")
    private boolean isActive;

}
