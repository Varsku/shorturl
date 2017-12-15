package com.hiqprogram.shorturl.domain;

import org.springframework.data.repository.CrudRepository;

public interface UrlRepository extends CrudRepository<UrlEntity, Long> {

    UrlEntity findByShortUrl(String shortUrl);
}
