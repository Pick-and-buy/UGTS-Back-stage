package com.ugts.statistic;

import java.time.LocalDate;
import java.util.List;

import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.user.dto.GeneralUserInformationDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticServiceImpl implements StatisticService {

    @NonFinal
    @PersistenceContext
    private EntityManager entityManager;

    LocalDate endDate = LocalDate.now();

    /**
     * Calculates the total amount of successful transactions that occurred on a specific date.
     *
     * @param date The date for which the total amount of successful transactions is calculated
     * @return The total amount of successful transactions on the given date
     */
    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public double getTotalAmountOfSuccessfulTransactionsInADay(LocalDate date) {
        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT SUM(t.amount) FROM Transaction t WHERE t.createDate = :date AND t.transactionStatus = :status",
                Double.class);
        query.setParameter("date", date);
        query.setParameter("status", TransactionStatus.SUCCESS);
        return query.getSingleResult();
    }

    /**
     * Calculates the total amount of successful transactions that occurred within the last week.
     *
     * @return The total amount of successful transactions in the past week
     */
    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public double getTotalAmountOfSuccessfulTransactionsInAWeek() {
        LocalDate startDate = LocalDate.now().minusDays(7);

        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionStatus = :status " +
                        "AND t.createDate BETWEEN :startDate AND :endDate", Double.class);

        query.setParameter("status", TransactionStatus.SUCCESS);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getSingleResult();
    }

    /**
     * Calculates the total amount of successful transactions that occurred within the last month.
     *
     * @return The total amount of successful transactions in the past month
     */
    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public double getTotalAmountOfSuccessfulTransactionsInAMonth() {
        LocalDate startDate = LocalDate.now().minusMonths(1);

        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionStatus = :status " +
                        "AND t.createDate BETWEEN :startDate AND :endDate", Double.class);

        query.setParameter("status", TransactionStatus.SUCCESS);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getSingleResult();
    }

    /**
     * Calculates the total amount of successful transactions that occurred within the last year.
     *
     * @return The total amount of successful transactions in the past year
     */
    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public double getTotalAmountOfSuccessfulTransactionsInAYear() {
        LocalDate startDate = LocalDate.now().minusYears(1);

        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionStatus = :status " +
                        "AND t.createDate BETWEEN :startDate AND :endDate", Double.class);

        query.setParameter("status", TransactionStatus.SUCCESS);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getSingleResult();
    }


    @Override
    public int getToTalTransactionsCountByDateAndTransactionType() {
        return 0;
    }

    @Override
    public int getToTalTransactionsCountByMonthAndTransactionType() {
        return 0;
    }

    @Override
    public int getToTalTransactionsCountByYearAndTransactionType() {
        return 0;
    }

    @Override
    public int getToTalTransactionsCountByWeekAndTransactionType() {
        return 0;
    }

    @Override
    public List<GeneralUserInformationDto> getFiveUsersWithHighestTransactionAmountPerDate() {
        return List.of();
    }

    @Override
    public List<GeneralUserInformationDto> getFiveUsersWithHighestTransactionAmountPerMonth() {
        return List.of();
    }

    @Override
    public List<GeneralUserInformationDto> getFiveUsersWithHighestTransactionAmountPerYear() {
        return List.of();
    }

    @Override
    public List<GeneralUserInformationDto> getFiveUsersWithHighestTransactionAmountPerWeek() {
        return List.of();
    }

    @Override
    public int countNewUserByDate() {
        return 0;
    }

    @Override
    public int countNewUserByMonth() {
        return 0;
    }

    @Override
    public int countNewUserByYear() {
        return 0;
    }

    @Override
    public int countNewUserByWeek() {
        return 0;
    }
}
