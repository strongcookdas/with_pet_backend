package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.enums.Category;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
@ToString
@Table(name = "user_diary")
public class UserDiary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDiaryId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;
    @NotNull
    private String title;
    @NotNull
    @Lob
    private String content;
    private String media;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "dogId", nullable = false)
    Dog dog;

    public static UserDiary of(DiaryRequest diaryRequest, Dog dog, User user) {
        return UserDiary.builder()
                .category(diaryRequest.getCategory())
                .title(diaryRequest.getTitle())
                .content(diaryRequest.getContentBody())
                .media(diaryRequest.getDogImgToday())
                .user(user)
                .dog(dog)
                .build();
    }

    public void update(DiaryRequest diaryRequest, Dog dog) {
        this.category = diaryRequest.getCategory();
        this.content = diaryRequest.getContentBody();
        this.title = diaryRequest.getTitle();
        this.dog = dog;
        this.media = diaryRequest.getDogImgToday();
    }
}
