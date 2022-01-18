package net.sunshow.badge.gateway.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
