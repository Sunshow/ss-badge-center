package net.sunshow.badge.gateway.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EntryController {

    @RequestMapping("/{store}")
    public void testStore(@PathVariable String store, HttpServletRequest request) {
        System.out.println("Without slash: " + store);
    }

    @RequestMapping("/{store}/")
    public void testBadge(@PathVariable String store, HttpServletRequest request) {
        System.out.println("With slash: " + store);
    }

    @RequestMapping("/{store}/**")
    public void test(@PathVariable String store, HttpServletRequest request) {
        System.out.println(store);
        String uri = (String)
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        System.out.println(uri);
    }

    @DeleteMapping("/{store}/test")
    public void testDelete(@PathVariable String store, @RequestBody String body, HttpServletRequest request) {
        System.out.println("Delete body: " + body);
    }
}
