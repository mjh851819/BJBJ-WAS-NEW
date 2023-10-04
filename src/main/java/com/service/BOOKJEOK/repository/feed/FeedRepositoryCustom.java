package com.service.BOOKJEOK.repository.feed;

import com.service.BOOKJEOK.domain.Feed;
import com.service.BOOKJEOK.dto.feed.FeedResponseDto;

import static com.service.BOOKJEOK.dto.feed.FeedResponseDto.*;

public interface FeedRepositoryCustom {

    Feed findByIdDetail(Long feedId);
}
