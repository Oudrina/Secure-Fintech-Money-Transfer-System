package com.audrina.security.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public  String getAllProducts(){
        return "All Products";
    }


    @DeleteMapping("/{id}")
    public  String deleteProducts( @PathVariable  Long id){
        return "    Only an ADMIN can access this endpoint\n";
    }
}
