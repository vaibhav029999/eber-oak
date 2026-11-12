package com.eberandoak.api.entity;

import jakarta.persistence.*;

@Entity @Table(name="categories", uniqueConstraints=@UniqueConstraint(name="uk_categories_name",columnNames="name"))
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=70) private String name;
    @Column(nullable=false,length=90) private String slug;
    @Column(name="image_url",length=1000) private String imageUrl;
    public Category(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getSlug(){return slug;} public void setSlug(String v){slug=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
}
