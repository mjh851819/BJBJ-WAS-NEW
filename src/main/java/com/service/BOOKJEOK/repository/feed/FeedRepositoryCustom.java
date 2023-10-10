package com.service.BOOKJEOK.repository.feed;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.domain.club.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

public interface FeedRepositoryCustom {

    Feed findByIdDetail(Long feedId);

    Page<FeedSearchResDto> findClubFeedList(Long clubId, String sortBy, Pageable pageable);

    Page<FeedSearchResDto> findFeedList(String sortBy, Pageable pageable);

    Page<FeedSearchResDto> findUserFeedList(Long userId, String sortBy, Pageable pageable);

    List<Long> findIdsByClub(Club club);

    void deleteByFeedIds(List<Long> ids);

    void deleteFeedById(Long feedId);

    List<FeedSearchResDto> find4FeedList(String sortBy);
}
