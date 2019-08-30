package com.gadis.gadis.controller;

import com.gadis.gadis.lib.core.Cache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stormstout-Chen
 */

@RestController
@RequestMapping(value = "/gadis")
public class CacheController {

        @RequestMapping(value = "/set", method = RequestMethod.POST, produces = "application/json")
    public String set(@RequestParam String k,
                      @RequestParam String value,
                      @RequestParam Long time) {
        return Cache.set(k, value, time).toString();
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, produces = "application/json")
    public String get(@RequestParam String k) {
        return Cache.get(k) == null ? null : Cache.get(k).toString();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST, produces = "application/json")
    public void remove(@RequestParam String k) {
        Cache.remove(k);
    }

    @RequestMapping(value = "/flush", method = RequestMethod.POST, produces = "application/json")
    public void removeAll() {
        Cache.flush();
    }

}
