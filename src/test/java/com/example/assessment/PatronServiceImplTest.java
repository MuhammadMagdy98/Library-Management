package com.example.assessment;

import com.example.assessment.domains.Patron;
import com.example.assessment.dtos.PatronDTO;
import com.example.assessment.exceptions.LibraryError;
import com.example.assessment.exceptions.LibraryException;
import com.example.assessment.repositories.PatronRepository;
import com.example.assessment.services.PatronServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatronServiceImplTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronServiceImpl patronService;

    private Patron patron;
    private PatronDTO patronDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setContactInfo("johndoe@example.com");

        patronDTO = new PatronDTO();
        patronDTO.setId(1L);
        patronDTO.setName("John Doe");
        patronDTO.setContactInfo("johndoe@example.com");
    }

    @Test
    void testGetPatrons() {
        when(patronRepository.findAll()).thenReturn(Flux.just(patron));

        Mono<List<PatronDTO>> result = patronService.getPatrons();

        StepVerifier.create(result)
                .expectNextMatches(patrons -> patrons.size() == 1 && patrons.get(0).getId().equals(patron.getId()))
                .verifyComplete();

        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testGetPatronById_Found() {
        when(patronRepository.findById(1L)).thenReturn(Mono.just(patron));

        Mono<PatronDTO> result = patronService.getPatronById(1L);

        StepVerifier.create(result)
                .expectNextMatches(patronDTO -> patronDTO.getId().equals(patron.getId()))
                .verifyComplete();

        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPatronById_NotFound() {
        when(patronRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<PatronDTO> result = patronService.getPatronById(1L);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof LibraryException &&
                        ((LibraryException) throwable).getError() == LibraryError.PATRON_NOT_FOUND)
                .verify();

        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testAddPatron() {
        when(patronRepository.save(any(Patron.class))).thenReturn(Mono.just(patron));

        Mono<PatronDTO> result = patronService.addPatron(patronDTO);

        StepVerifier.create(result)
                .expectNextMatches(savedPatron -> savedPatron.getId().equals(patron.getId()))
                .verifyComplete();

        verify(patronRepository, times(1)).save(any(Patron.class));
    }

    @Test
    void testUpdatePatronById_Found() {
        when(patronRepository.findById(1L)).thenReturn(Mono.just(patron));
        when(patronRepository.save(any(Patron.class))).thenReturn(Mono.just(patron));

        Mono<PatronDTO> result = patronService.updatePatronById(1L, patronDTO);

        StepVerifier.create(result)
                .expectNextMatches(updatedPatron -> updatedPatron.getId().equals(patron.getId()))
                .verifyComplete();

        verify(patronRepository, times(1)).findById(1L);
        verify(patronRepository, times(1)).save(any(Patron.class));
    }

    @Test
    void testUpdatePatronById_NotFound() {
        when(patronRepository.findById(1L)).thenReturn(Mono.empty());

        Mono<PatronDTO> result = patronService.updatePatronById(1L, patronDTO);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof LibraryException &&
                        ((LibraryException) throwable).getError() == LibraryError.PATRON_NOT_FOUND)
                .verify();

        verify(patronRepository, times(1)).findById(1L);
    }

    @Test
    void testDeletePatronById_Found() {
        when(patronRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(patronRepository.deleteById(1L)).thenReturn(Mono.empty());

        Mono<Boolean> result = patronService.deletePatronById(1L);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatronById_NotFound() {
        when(patronRepository.existsById(1L)).thenReturn(Mono.just(false));

        Mono<Boolean> result = patronService.deletePatronById(1L);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(patronRepository, times(1)).existsById(1L);
        verify(patronRepository, never()).deleteById(1L);
    }
}