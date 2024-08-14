package com.example.assessment.services;

import com.example.assessment.domains.Patron;
import com.example.assessment.dtos.PatronDTO;
import com.example.assessment.exceptions.LibraryError;
import com.example.assessment.exceptions.LibraryException;
import com.example.assessment.repositories.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PatronServiceImpl implements PatronService {

    @Autowired
    private PatronRepository patronRepository;

    private PatronDTO getPatronDTO(Patron patrons) {
        PatronDTO patron = new PatronDTO();

        patron.setId(patrons.getId());
        patron.setName(patrons.getName());
        patron.setContactInfo(patrons.getContactInfo());
        return patron;
    }

    @Override
    public Mono<List<PatronDTO>> getPatrons() {
        return patronRepository.findAll().map(this::getPatronDTO).collectList();
    }

    @Override
    public Mono<PatronDTO> getPatronById(Long id) {
        return patronRepository.findById(id).map(this::getPatronDTO)
                .switchIfEmpty(Mono.error(new LibraryException(LibraryError.PATRON_NOT_FOUND)));
    }

    @Override
    public Mono<PatronDTO> addPatron(PatronDTO patron) {
        return Mono.fromSupplier(() -> {
            Patron myPatron = new Patron();
            myPatron.setName(patron.getName());
            myPatron.setContactInfo(patron.getContactInfo());
            return myPatron;
        })
                .flatMap(patronRepository::save)
                .doOnNext(savedPatron -> {
                    System.out.println("Saved Book ID: " + savedPatron.toString());
                })
                .map(this::getPatronDTO);
    }

    @Override
    public Mono<PatronDTO> updatePatronById(Long id, PatronDTO updatedPatron) {
        return patronRepository.findById(id).flatMap(patron -> {

            patron.setName(updatedPatron.getName());
            patron.setContactInfo(updatedPatron.getContactInfo());

            return patronRepository.save(patron);
        }).map(this::getPatronDTO).switchIfEmpty(Mono.error(new LibraryException(LibraryError.PATRON_NOT_FOUND)));
    }

    @Override
    public Mono<Boolean> deletePatronById(Long id) {
        return patronRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.just(false);
            }
            return patronRepository.deleteById(id).then(Mono.just(true));
        });
    }
}
