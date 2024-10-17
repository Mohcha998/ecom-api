package com.ecom.controller;

import com.ecom.models.entities.Menu;
import com.ecom.models.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return ResponseEntity.ok(menus);
    }

    @PostMapping
    public ResponseEntity<String> addMenu(@RequestBody Menu menu) {
        menuRepository.save(menu);
        return ResponseEntity.ok("Menu added successfully");
    }
}
