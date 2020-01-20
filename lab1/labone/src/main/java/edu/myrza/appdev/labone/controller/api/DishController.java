package edu.myrza.appdev.labone.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dish")
public class DishController {

    @PostMapping
    public ResponseEntity<?> create(){ return null; }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id){ return null; }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){ return null; }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){ return null; }

    @GetMapping("/pname/{partname}")
    public ResponseEntity<?> findByPartName(@PathVariable String partname){ return null; }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){ return null; }

}
