package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.model.ListingStatus;
import com.endava.marketplace.backend.repository.ListingStatusRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Listing Status Service Unit Test")
public class ListingStatusServiceTests {
    @Mock
    ListingStatusRepository listingStatusRepository;

    private ListingStatusService listingStatusService;

    @BeforeEach
    void setUp(){
        listingStatusService = new ListingStatusService(listingStatusRepository);
    }

    @Test
    public void whenClassCreated_thenGetAllListingStatuses() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        ListingStatus available = ListingStatus.builder()
                .id(1L)
                .name("Available")
                .build();

        ListingStatus blocked = ListingStatus.builder()
                .id(2L)
                .name("Blocked")
                .build();

        List<ListingStatus> data = List.of(available, blocked);
        when(listingStatusRepository.findAll()).thenReturn(data);

        // When
        Method postConstruct =  ListingStatusService.class.getDeclaredMethod("getAllListingStatuses",null);
        postConstruct.setAccessible(true);
        postConstruct.invoke(listingStatusService);

        Map<String, ListingStatus> results = listingStatusService.getListingStatuses();

        // Then
        verify(listingStatusRepository, times(1)).findAll();
        Assertions.assertThat(results).isNotNull().isNotEmpty();
    }
}
