package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
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
import org.springframework.data.annotation.LastModifiedDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
@ToString
@Table(name = "user_diary")
public class UserDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDiaryId;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
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
    private Dog dog;

    @NotNull
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    private LocalDateTime deletedAt;

    public static UserDiary of(DiaryRequest diaryRequest, Dog dog, User user, Category category) {
        return UserDiary.builder()
                .category(category)
                .title(diaryRequest.getTitle())
                .content(diaryRequest.getContentBody())
                .media(diaryRequest.getDogImgToday())
                .user(user)
                .dog(dog)
                .createdAt(diaryRequest.getCreatedAt())
                .build();
    }

    public void update(DiaryRequest diaryRequest, Dog dog, Category category) {
        this.category = category;
        this.content = diaryRequest.getContentBody();
        this.title = diaryRequest.getTitle();
        this.dog = dog;
        this.media = diaryRequest.getDogImgToday();
    }
}