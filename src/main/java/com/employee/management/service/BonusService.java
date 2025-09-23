
package com.employee.management.service;

import com.employee.management.model.Employee;
import com.employee.management.model.PerformanceRating;
import com.employee.management.repository.PerformanceRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonusService {
    private final PerformanceRatingRepository performanceRatingRepository;

    @Autowired
    public BonusService(PerformanceRatingRepository performanceRatingRepository) {
        this.performanceRatingRepository = performanceRatingRepository;
    }

    public double calculateBonus(Employee employee) {
        List<PerformanceRating> ratings = performanceRatingRepository.findByEmployeeId(employee.getId());
        if (ratings.isEmpty()) {
            return 0.0; // No ratings, no bonus
        }

        // Get the latest rating
        PerformanceRating latestRating = ratings.get(ratings.size() - 1);
        int rating = latestRating.getRating();

        // Calculate bonus as a percentage of salary based on rating
        double bonusPercentage;
        switch (rating) {
            case 5:
                bonusPercentage = 0.20; // 20% bonus for rating 5
                break;
            case 4:
                bonusPercentage = 0.15; // 15% bonus for rating 4
                break;
            case 3:
                bonusPercentage = 0.10; // 10% bonus for rating 3
                break;
            case 2:
                bonusPercentage = 0.05; // 5% bonus for rating 2
                break;
            default:
                bonusPercentage = 0.0; // No bonus for rating 1 or below
        }

        return employee.getSalary() * bonusPercentage;
    }
}
