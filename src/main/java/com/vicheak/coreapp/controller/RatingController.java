package com.vicheak.coreapp.controller;

import com.vicheak.coreapp.dto.RatingDto;
import com.vicheak.coreapp.dto.RatingTransactionDto;
import com.vicheak.coreapp.dto.UpdateRatingDto;
import com.vicheak.coreapp.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/{studentUuid}")
    public RatingDto loadRatingByStudentUuid(@PathVariable String studentUuid){
        return ratingService.loadRatingByStudentUuid(studentUuid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createNewRating(@RequestBody @Valid RatingTransactionDto ratingTransactionDto){
        ratingService.createNewRating(ratingTransactionDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{studentUuid}")
    public void updateRating(@PathVariable String studentUuid,
                             @RequestBody @Valid UpdateRatingDto updateRatingDto){
        ratingService.updateRating(studentUuid, updateRatingDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{studentUuid}")
    public void deleteRating(@PathVariable String studentUuid){
        ratingService.deleteRating(studentUuid);
    }

}
