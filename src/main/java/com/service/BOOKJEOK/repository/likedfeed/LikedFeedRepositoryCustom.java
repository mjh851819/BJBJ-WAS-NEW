package com.service.BOOKJEOK.repository.likedfeed;

import com.service.BOOKJEOK.domain.LikedFeed;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;
import com.service.BOOKJEOK.dto.likedfeed.LikedFeedResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

public interface LikedFeedRepositoryCustom {
    Page<FeedSearchResDto> searchFeedList(Long userId, Pageable pageable);

    List<LikedFeedResponseDto.LikedFeedIdResDto> searchFeedIdList(Long userId);
}
