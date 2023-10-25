package com.service.BOOKJEOK.service;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.domain.user.User;
import com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto;
import com.service.BOOKJEOK.handler.ex.CustomApiException;
import com.service.BOOKJEOK.handler.ex.ExMessage;
import com.service.BOOKJEOK.repository.user.UserRepository;
import com.service.BOOKJEOK.repository.feed.FeedRepository;
import com.service.BOOKJEOK.repository.likedfeed.LikedFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;
import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedRequestDto.*;
import static com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikedFeedService {

    private final LikedFeedRepository likedFeedRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    @Transactional
    public void createLike(LikedFeedCreateReqDto req) {
        User userPS = userRepository.findById(req.getUserId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_USER));
        Feed feedPS = feedRepository.findById(req.getFeedId()).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_FEED));

        LikedFeed likedFeed = req.toEntity(userPS, feedPS);
        likedFeedRepository.save(likedFeed);
    }

    @Transactional
    public void deleteLike(Long feedId, Long userId) {
        LikedFeed likedFeed = likedFeedRepository.findByFeedAndUser(feedId, userId).orElseThrow(() -> new CustomApiException(ExMessage.NOT_FOUND_LIKE));

        likedFeedRepository.delete(likedFeed);
    }

    public FeedSearchPageResDto searchFeedList(Long userId, Pageable pageable) {

        Page<FeedSearchResDto> res = likedFeedRepository.searchFeedList(userId, pageable);

        return new FeedSearchPageResDto((int) res.getTotalElements(), res.getContent());
    }

    public LikedFeedIdListResDto getLikedFeedIds(Long userId) {

        List<LikedFeedIdResDto> res = likedFeedRepository.searchFeedIdList(userId);

        return new LikedFeedIdListResDto(res.size(), res);
    }
}
