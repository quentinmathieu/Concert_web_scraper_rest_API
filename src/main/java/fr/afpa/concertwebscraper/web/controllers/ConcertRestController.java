package fr.afpa.concertwebscraper.web.controllers;

import java.io.IOException;
import java.util.List;

import fr.afpa.concertwebscraper.repositories.ConcertRepository;
import fr.afpa.concertwebscraper.entities.Concert;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/concerts")
public class ConcertRestController {
 
    private ConcertRepository concertRepository;

    public ConcertRestController(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }



    // @GetMapping
    // public List<Account> getAll() {
    //     return (List<Account>) accountRepository.findAll(); 
    // }
    @GetMapping
    public List<Concert> getAll() throws IOException {
        return (List<Concert>) concertRepository.findAll(); 
    }

   
    // @GetMapping("/{id}")
    // public Optional<Account> getOne(@PathVariable long id) {
    //     return accountRepository.findById(id);
    // }


    // @PostMapping
    // @ResponseStatus(HttpStatus.CREATED)
    // public Account create(@RequestBody Account account) {
    //     accountRepository.save(account);
    //     return account;
    // }

    // @SuppressWarnings("unchecked")
    // @PutMapping("/{id}")
    // public ResponseEntity<Account> update(@PathVariable long id, @RequestBody Account account) {
    //     Optional<Account> oldAccount = accountRepository.findById(id);
    //     if (!oldAccount.isPresent()){
    //         System.out.println("meh");
    //         return (ResponseEntity<Account>) ResponseEntity.badRequest();
    //     }
    //     return ResponseEntity.ok(accountRepository.save(account));
    // }

    // public boolean remove(@PathVariable long id, HttpServletResponse response) {
    //     Optional<Account> oldAccount = accountRepository.findById(id);
    //     if (!oldAccount.isPresent()){
    //         return false;
    //     }
    //     response.setStatus(HttpServletResponse.SC_OK);
    //     return true;
    // }
}
