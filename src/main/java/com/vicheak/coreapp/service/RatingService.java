package com.vicheak.coreapp.service;

import com.vicheak.coreapp.dto.RatingDto;
import com.vicheak.coreapp.dto.RatingTransactionDto;
import com.vicheak.coreapp.dto.UpdateRatingDto;

public interface RatingService {

    RatingDto loadRatingByStudentUuid(String studentUuid);

    void createNewRating(RatingTransactionDto ratingTransactionDto);

    void updateRating(String studentUuid, UpdateRatingDto updateRatingDto);

    void deleteRating(String studentUuid);

}
