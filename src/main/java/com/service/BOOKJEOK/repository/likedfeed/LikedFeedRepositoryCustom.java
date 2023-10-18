package com.service.BOOKJEOK.repository.likedfeed;

import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

public interface LikedFeedRepositoryCustom {
    Page<FeedSearchResDto> searchFeedList(Long userId, Pageable pageable);

    Optional<LikedFeed> findByFeedAndUser(Long feedId, Long userId);
}
