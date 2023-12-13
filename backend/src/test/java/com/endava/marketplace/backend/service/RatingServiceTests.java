package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.model.Endavan;
import com.endava.marketplace.backend.model.Rating;
import com.endava.marketplace.backend.repository.RatingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Rating Service Unit Test")
@ExtendWith(MockitoExtension.class)
public class RatingServiceTests {
    @Mock
    RatingRepository ratingRepository;
    private RatingService ratingService;

    private Endavan endavan;
    Long ratingId = 1L;
    private Rating rating;

    @BeforeEach
    void setUp(){
        ratingService = new RatingService(ratingRepository);

        endavan = Endavan.builder()
                .id(1L)
                .name("Endavan")
                .email("email@example.com")
                .admin(false)
                .build();

        rating = Rating.builder()
                .id(ratingId)
                .score(1.0)
                .quantity(1)
                .endavan(endavan)
                .build();
    }

    @Test
    public void givenRatingId_whenLoadRating_thenReturnsRating(){
        // Given
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.ofNullable(rating));

        // When
        Rating foundRating = ratingService.loadRating(ratingId);

        // Then
        Assertions.assertThat(foundRating).isNotNull();
        Assertions.assertThat(foundRating.getId()).isEqualTo(rating.getId());
    }

    @Test
    public void givenWrongRatingId_whenLoadRating_thenReturnsEntityNotFoundException(){
        // Given
        Long wrongId = 2L;

        when(ratingRepository.findById(wrongId)).thenReturn(Optional.empty());

        // When - Then
        Assertions.assertThatThrownBy(
                        () -> ratingService.loadRating(wrongId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Rating with id " + wrongId + " wasn't found");
    }

    @Test
    public void givenRating_whenUpdateRating_thenRatingIsUpdated(){
        // Given
        Rating updatedRating = Rating.builder()
                .id(ratingId)
                .score(1.5)
                .quantity(2)
                .endavan(endavan)
                .build();

        when(ratingRepository.save(updatedRating)).thenReturn(updatedRating);

        // When
        ratingService.updateRating(updatedRating);

        // Then
        verify(ratingRepository, times(1)).save(updatedRating);
    }

}
