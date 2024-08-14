package com.ugts.statistic;

import java.time.LocalDate;

import com.ugts.common.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {

    StatisticService statisticService;

    @GetMapping("/total-amount-per-date")
    public ApiResponse<Double> getToTalTransactionsAmountPerDateByTransactionStatus(
            @RequestParam LocalDate date) {
        var result = statisticService.getTotalAmountOfSuccessfulTransactionsInADay(date);
        return ApiResponse.<Double>builder().message("Success").result(result).build();
    }

    @GetMapping("/total-amount-per-week")
    public ApiResponse<Double> getToTalTransactionsAmountPerWeekByTransactionStatus() {
        var result = statisticService.getTotalAmountOfSuccessfulTransactionsInAWeek();
        return ApiResponse.<Double>builder().message("Success").result(result).build();
    }

    @GetMapping("/total-amount-per-month")
    public ApiResponse<Double> getToTalTransactionsAmountPerMonthByTransactionStatus() {
        var result = statisticService.getTotalAmountOfSuccessfulTransactionsInAMonth();
        return ApiResponse.<Double>builder().message("Success").result(result).build();
    }

    @GetMapping("/total-amount-per-year")
    public ApiResponse<Double> getToTalTransactionsAmountPerYearByTransactionStatus() {
        var result = statisticService.getTotalAmountOfSuccessfulTransactionsInAYear();
        return ApiResponse.<Double>builder().message("Success").result(result).build();
    }
}
