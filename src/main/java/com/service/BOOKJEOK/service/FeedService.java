package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.club.ClubRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.service.BOOKJEOK.dto.feed.FeedRequestDto.*;
import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

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
        feedRepository.deleteFeedById(feedId);
    }

    public FeedSearchDetailResDto searchFeed(Long feedId) {

        Feed feedPS = feedRepository.findByIdDetail(feedId);

        FeedSearchDetailResDto res = new FeedSearchDetailResDto(feedPS);

        return res;
    }

    public FeedSearchPageResDto searchFeedList(String sortBy, Pageable pageable) {

        Page<FeedSearchResDto> res = feedRepository.findFeedList(sortBy, pageable);

        return new FeedSearchPageResDto((int) res.getTotalElements(), res.getContent());
    }

    public FeedSearchPageResDto searchClubFeedList(Long clubId, String sortBy, Pageable pageable) {

        Page<FeedSearchResDto> res = feedRepository.findClubFeedList(clubId, sortBy, pageable);

        return new FeedSearchPageResDto((int) res.getTotalElements(), res.getContent());
    }


    public FeedSearchPageResDto searchUserFeedList(Long userId, Pageable pageable) {

        Page<FeedSearchResDto> res = feedRepository.findUserFeedList(userId, pageable);

        return new FeedSearchPageResDto((int) res.getTotalElements(), res.getContent());
    }

    public FeedSearchPageResDto searchFeedForMain(String sortBy) {
        List<FeedSearchResDto> res = feedRepository.find4FeedList(sortBy);

        return new FeedSearchPageResDto(res.size(), res);
    }
}
