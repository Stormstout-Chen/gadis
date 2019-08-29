package com.gadis.gadis.controller;

import com.gadis.gadis.lib.core.Cache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {

    @RequestMapping("/gadis/set")
    public String set(@RequestParam String k,
                      @RequestParam String value,
                      Long time){
        return Cache.set(k,value,time).toString();
    }

    @RequestMapping("/gadis/get")
    public String get(@RequestParam String k){
        return Cache.get(k) == null ? null : Cache.get(k).toString();
    }

    @RequestMapping("/gadis/remove")
    public void remove(@RequestParam String k){
        Cache.remove(k);
    }

    @RequestMapping("/gadis/flush")
    public void removeAll(){
        Cache.flush();
    }

}
