package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.SaleStatus;
import com.endava.marketplace.backend.repository.SaleStatusRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SaleStatusServiceTests {
    @Mock
    SaleStatusRepository saleStatusRepository;

    private SaleStatusService saleStatusService;

    @BeforeEach
    void setUp(){
        saleStatusService = new SaleStatusService(saleStatusRepository);
    }

    @Test
    public void givenSaleStatusId_whenFindSaleStatusById_thenReturnsStatus(){
        Long statusId = 1L;

        SaleStatus saleStatus = SaleStatus.builder()
                .id(1L)
                .name("Status #1")
                .sales(new HashSet<>())
                .build();

        when(saleStatusRepository.findById(statusId.intValue())).thenReturn(Optional.ofNullable(saleStatus));
        Optional<SaleStatus> foundStatus = saleStatusService.findSaleStatusById(statusId);

        Assertions.assertThat(foundStatus).isNotNull();
        Assertions.assertThat(saleStatus.getId()).isEqualTo(foundStatus.get().getId());
    }

    @Test
    public void whenFindAllSaleStatus_thenReturnsAllAvailableStatus(){
        List<SaleStatus> allStatus = new ArrayList<>();
        SaleStatus statusOne = SaleStatus.builder()
                .id(1L)
                .name("Status #1")
                .sales(new HashSet<>())
                .build();
        SaleStatus statusTwo = SaleStatus.builder()
                .id(2L)
                .name("Status #2")
                .sales(new HashSet<>())
                .build();
        allStatus.add(statusOne);
        allStatus.add(statusTwo);

        when(saleStatusRepository.findAll()).thenReturn(allStatus);
        List<SaleStatus> foundStatus = saleStatusService.findAllSaleStatus();

        Assertions.assertThat(foundStatus).isNotNull();
        Assertions.assertThat(allStatus).isEqualTo(foundStatus);
    }
}
