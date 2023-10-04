package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.feed.FeedRequestDto;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.service.BOOKJEOK.dto.feed.FeedRequestDto.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public void createFeed(FeedCreateReqDto req) {
        User userPS = userRepository.findById(req.getUserId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_USER));
        Club clubPS = clubRepository.findById(req.getClubId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));

        Feed feed = req.toEntity(userPS, clubPS);
        feedRepository.save(feed);
    }

    @Transactional
    public void updateFeed(FeedUpdateReqDto req) {
        userRepository.findById(req.getUserId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_USER));
        clubRepository.findById(req.getClubId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_CLUB));
        Feed feedPS = feedRepository.findById(req.getFeedId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_FEED));

        feedPS.update(req);
    }

    @Transactional
    public void deleteFeed(Long feedId) {
        Feed feedPS = feedRepository.findById(feedId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_FEED));

        // issue: delete()를 호출했을때 나가는 쿼리수를 체크해 봐야함.
        feedRepository.delete(feedPS);
    }
}
