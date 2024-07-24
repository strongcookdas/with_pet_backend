package com.ajou_nice.with_pet.diary.model.entity;

import com.ajou_nice.with_pet.diary.model.dto.user.UserDiaryPostRequest;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.diary.model.dto.DiaryModifyRequest;
import com.ajou_nice.with_pet.diary.model.dto.DiaryRequest;
import com.ajou_nice.with_pet.domain.entity.User;
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

import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
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
@Table(name = "diary")
public class Diary{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category diaryCategory;
    @NotNull
    private String diaryTitle;
    @NotNull
    @Lob
    private String diaryContent;
    @Lob
    private String diaryMedia;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    @ManyToOne
    @JoinColumn(name = "dogId", nullable = false)
    private Dog dog;

    @NotNull
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    private LocalDateTime deletedAt;

    public static Diary of(UserDiaryPostRequest userDiaryPostRequest, Dog dog, User user, Category category) {
        return Diary.builder()
                .diaryCategory(category)
                .diaryTitle(userDiaryPostRequest.getUserDiaryTitle())
                .diaryContent(userDiaryPostRequest.getUserDiaryContent())
                .diaryMedia(userDiaryPostRequest.getUserDiaryDogImg())
                .user(user)
                .dog(dog)
                .createdAt(userDiaryPostRequest.getUserDiaryCreatedAt())
                .build();
    }

    public static Diary of(DiaryRequest diaryRequest, Dog dog, User user, Category category,
            PetSitter petSitter) {
        return Diary.builder()
                .diaryCategory(category)
                .diaryTitle(diaryRequest.getTitle())
                .diaryContent(diaryRequest.getContentBody())
                .diaryMedia(diaryRequest.getDogImgToday())
                .user(user)
                .dog(dog)
                .createdAt(diaryRequest.getCreatedAt())
                .petSitter(petSitter)
                .build();
    }

    public static Diary simpleUserDiaryForTest(Category category, String title, String content,
            User user, Dog dog, LocalDate localDate, PetSitter petSitter) {
        return Diary.builder()
                .diaryCategory(category)
                .diaryTitle(title)
                .diaryContent(content)
                .user(user)
                .dog(dog)
                .createdAt(localDate)
                .petSitter(petSitter)
                .build();
    }

    public void update(DiaryRequest diaryRequest, Dog dog, Category category) {
        this.diaryCategory = category;
        this.diaryContent = diaryRequest.getContentBody();
        this.diaryTitle = diaryRequest.getTitle();
        this.dog = dog;
        this.diaryMedia = diaryRequest.getDogImgToday();
        this.createdAt = diaryRequest.getCreatedAt();
    }

    public void update(DiaryModifyRequest diaryModifyRequest, Category category) {
        this.diaryCategory = category;
        this.diaryContent = diaryModifyRequest.getContentBody();
        this.diaryTitle = diaryModifyRequest.getTitle();
        this.dog = dog;
        this.diaryMedia = diaryModifyRequest.getDogImgToday();
        this.createdAt = diaryModifyRequest.getCreatedAt();
    }
}
