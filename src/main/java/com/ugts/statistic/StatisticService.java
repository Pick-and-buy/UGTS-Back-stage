package com.ugts.statistic;

import java.time.LocalDate;
import java.util.List;

import com.ugts.user.dto.GeneralUserInformationDto;

public interface StatisticService {
    double getTotalAmountOfSuccessfulTransactionsInADay(LocalDate date);

    double getTotalAmountOfSuccessfulTransactionsInAWeek();

    double getTotalAmountOfSuccessfulTransactionsInAMonth();

    double getTotalAmountOfSuccessfulTransactionsInAYear();

    int getToTalTransactionsCountByDateAndTransactionType();

    int getToTalTransactionsCountByMonthAndTransactionType();

    int getToTalTransactionsCountByYearAndTransactionType();

    int getToTalTransactionsCountByWeekAndTransactionType();

    List<GeneralUserInformationDto> getFiveUsersWithHighestTransactionAmountPerDate();

    List<GeneralUserInformationDto> getFiveUsersWithHighestTransactionAmountPerMonth();

    List<GeneralUserInformationDto> getFiveUsersWithHighestTransactionAmountPerYear();

    List<GeneralUserInformationDto> getFiveUsersWithHighestTransactionAmountPerWeek();

    int countNewUserByDate();

    int countNewUserByMonth();

    int countNewUserByYear();

    int countNewUserByWeek();
}
