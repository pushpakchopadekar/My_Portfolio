package com.pushpak.portfolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "profile")
public class Profile {

    @Id
    private Long id = 1L;

    private String fullName;
    private String tagline;

    @Column(length = 2000)
    private String bio;

    private String email;
    private String phone;
    private String githubUrl;
    private String linkedinUrl;
    private String resumeUrl;

    // Profile photo
    private String photoUrl;

    // Hero section content (editable from admin)
    private String heroEyebrow;

    @Column(length = 500)
    private String heroHeadline;

    @Column(length = 1000)
    private String heroSub;

    // Stats shown under the hero
    private String statExperience;
    private String statCgpa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getHeroEyebrow() {
        return heroEyebrow;
    }

    public void setHeroEyebrow(String heroEyebrow) {
        this.heroEyebrow = heroEyebrow;
    }

    public String getHeroHeadline() {
        return heroHeadline;
    }

    public void setHeroHeadline(String heroHeadline) {
        this.heroHeadline = heroHeadline;
    }

    public String getHeroSub() {
        return heroSub;
    }

    public void setHeroSub(String heroSub) {
        this.heroSub = heroSub;
    }

    public String getStatExperience() {
        return statExperience;
    }

    public void setStatExperience(String statExperience) {
        this.statExperience = statExperience;
    }

    public String getStatCgpa() {
        return statCgpa;
    }

    public void setStatCgpa(String statCgpa) {
        this.statCgpa = statCgpa;
    }
}