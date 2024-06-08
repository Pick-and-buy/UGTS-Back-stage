package com.ugts.homepage.service;
// SlopeOneRecommender.java

import java.util.*;

import com.ugts.post.entity.Post;
import com.ugts.post.repository.PostRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlopeOneRecommender {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private final Map<Long, Map<Long, Double>> diffMatrix;
    private final Map<Long, Map<Long, Integer>> freqMatrix;

    public SlopeOneRecommender() {
        diffMatrix = new HashMap<>();
        freqMatrix = new HashMap<>();
    }

    public void buildDiffMatrix(List<User> users) {
        for (User user : users) {
            Map<Long, Double> userRatings = getUserRatings(user);

            for (Map.Entry<Long, Double> entry : userRatings.entrySet()) {
                if (!diffMatrix.containsKey(entry.getKey())) {
                    diffMatrix.put(entry.getKey(), new HashMap<>());
                    freqMatrix.put(entry.getKey(), new HashMap<>());
                }

                for (Map.Entry<Long, Double> entry2 : userRatings.entrySet()) {
                    int oldCount = freqMatrix.get(entry.getKey()).getOrDefault(entry2.getKey(), 0);
                    double oldDiff = diffMatrix.get(entry.getKey()).getOrDefault(entry2.getKey(), 0.0);
                    double observedDiff = entry.getValue() - entry2.getValue();

                    freqMatrix.get(entry.getKey()).put(entry2.getKey(), oldCount + 1);
                    diffMatrix.get(entry.getKey()).put(entry2.getKey(), oldDiff + observedDiff);
                }
            }
        }

        for (Long i : diffMatrix.keySet()) {
            for (Long j : diffMatrix.get(i).keySet()) {
                double oldValue = diffMatrix.get(i).get(j);
                int count = freqMatrix.get(i).get(j);
                diffMatrix.get(i).put(j, oldValue / count);
            }
        }
    }

    public Map<Long, Double> predict(User user) {
        Map<Long, Double> predictions = new HashMap<>();
        Map<Long, Integer> frequencies = new HashMap<>();
        Map<Long, Double> userRatings = getUserRatings(user);

        for (Long j : diffMatrix.keySet()) {
            frequencies.put(j, 0);
            predictions.put(j, 0.0);
        }

        for (Long j : userRatings.keySet()) {
            for (Long k : diffMatrix.keySet()) {
                try {
                    double newValue = (diffMatrix.get(k).get(j) + userRatings.get(j))
                            * freqMatrix.get(k).get(j);
                    predictions.put(k, predictions.get(k) + newValue);
                    frequencies.put(k, frequencies.get(k) + freqMatrix.get(k).get(j));
                } catch (NullPointerException e) {
                    // ignore
                }
            }
        }

        Map<Long, Double> cleanPredictions = new HashMap<>();
        for (Long j : predictions.keySet()) {
            if (frequencies.get(j) > 0) {
                cleanPredictions.put(j, predictions.get(j) / frequencies.get(j));
            }
        }

        return cleanPredictions;
    }

    private Map<Long, Double> getUserRatings(User user) {
        Map<Long, Double> ratings = new HashMap<>();
        user.getLikedPosts().forEach(post -> ratings.put(Long.valueOf(post.getId()), 1.0));
        user.getPurchasedPosts().forEach(post -> ratings.put(Long.valueOf(post.getId()), 2.0));
        user.getViewedPosts().forEach(post -> ratings.put(Long.valueOf(post.getId()), 0.5));
        return ratings;
    }

    public List<Post> recommend(User user) {
        List<User> allUsers = userRepository.findAll();
        buildDiffMatrix(allUsers);

        Map<Long, Double> predictedRatings = predict(user);
        List<Post> recommendations = new ArrayList<>();
        predictedRatings.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(10) // size recommendation list
                .forEach(entry -> recommendations.add(
                        postRepository.findById(String.valueOf(entry.getKey())).orElse(null)));

        return recommendations;
    }
}
