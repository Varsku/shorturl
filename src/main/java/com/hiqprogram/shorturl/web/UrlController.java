package com.hiqprogram.shorturl.web;


import com.hiqprogram.shorturl.domain.UrlEntity;
import com.hiqprogram.shorturl.domain.UrlRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
public class UrlController {

    @Autowired
    private UrlRepository repository;

    @RequestMapping(value="/urls", method = RequestMethod.GET)
    public List<UrlEntity> studentListRest() {
        return (List<UrlEntity>) repository.findAll();
    }

    @RequestMapping(value="/shorten", method = RequestMethod.POST)
    public UrlEntity shortenLongUrl(@RequestParam("url") String longUrl) throws Exception {
        // Add URL validator
        //Create new URL entity
        //Set the LONG URL given in the POST url to Entity
        //If the URL is valid, generate a shortUrl to it and save the entity to repo
        //Return the newly added URL entity
        //If the URL is not valid it will return an exception error
        final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        UrlEntity newUrl= new UrlEntity();
        newUrl.setLongUrl(longUrl);
        if (urlValidator.isValid(longUrl)) {
            String shortUrl = RandomStringUtils.randomAlphanumeric(16);
            newUrl.setShortUrl(shortUrl);
            repository.save(newUrl);
            return newUrl;
        } else {
            throw new Exception("URL should start with http:// OR https://");
        }

    }
    @RequestMapping(value="/find/{shortUrl}", method = RequestMethod.GET)
    public void findLongUrl(@PathVariable("shortUrl") String shortUrl, HttpServletResponse resp) throws Exception {
        //First check that the given shortUrl is found in repo
        //If the url is found. assign it's longUrl to a string
        //Automatically redirect to the longUrl
        //If the url is not found, show a bad request error with message
       if (repository.findByShortUrl(shortUrl) != null) {
           String url = repository.findByShortUrl(shortUrl).getLongUrl();
           resp.sendRedirect(url);
       }else {
           resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No URL found with given shortUrl");
       }
    }
}
