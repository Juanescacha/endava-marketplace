package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.SaleStatus;
import com.endava.marketplace.backend.repository.SaleStatusRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Sale Status Service Unit Tests")
public class SaleStatusServiceTests {
    @Mock
    SaleStatusRepository saleStatusRepository;

    private SaleStatusService saleStatusService;

    @BeforeEach
    void setUp() {
        saleStatusService = new SaleStatusService(saleStatusRepository);
    }

    @Test
    public void whenClassCreated_thenGetAllSaleSatuses() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        SaleStatus pending = SaleStatus.builder()
                .id(1L)
                .name("Pending")
                .build();

        SaleStatus cancelled = SaleStatus.builder()
                .id(2L)
                .name("Cancelled")
                .build();

        List<SaleStatus> data = List.of(pending, cancelled);

        when(saleStatusRepository.findAll()).thenReturn(data);
        // When
        Method postConstruct =  SaleStatusService.class.getDeclaredMethod("getAllSaleStatuses",null);
        postConstruct.setAccessible(true);
        postConstruct.invoke(saleStatusService);

        Map<String, SaleStatus> results = saleStatusService.getSaleStatuses();

        // Then
        verify(saleStatusRepository, times(1)).findAll();
        Assertions.assertThat(results).isNotNull().isNotEmpty();
    }
}
