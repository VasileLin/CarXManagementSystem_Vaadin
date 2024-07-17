package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.ServiceDto;
import com.vmoon.carx.entities.Service;
import com.vmoon.carx.mappers.ServiceMapper;
import com.vmoon.carx.repositories.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicesServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    Pageable pageable;

    @InjectMocks
    private ServicesServiceImpl servicesService;




    @Test
    void allServices_ShouldReturnPageableWithServices() {
        //Arrange
        Service service = Service.builder()
                .id(1L)
                .name("Service1")
                .price(20d)
                .build();

        Service service1 = Service.builder()
                .id(2L)
                .name("Service2")
                .price(24d)
                .build();

        List<Service> services = Arrays.asList(service,service1);
        Page<Service> servicePage = new PageImpl<>(services,pageable,services.size());
        when(serviceRepository.findAll(pageable)).thenReturn(servicePage);

        //Act
        Page<ServiceDto> servicesResult = servicesService.allServices(pageable);

        //Assert
        assertEquals(2, servicesResult.getContent().size());
        verify(serviceRepository,times(1)).findAll(pageable);
        assertEquals("Service1",servicesResult.getContent().get(0).getName());
        assertEquals("Service2",servicesResult.getContent().get(1).getName());
    }

    @Test
    void saveService_ShouldSaveUserAndReturnSavedUser() {
        //Arrange
        ServiceDto serviceDto = ServiceDto.builder()
                .name("ServiceSave")
                .price(124d)
                .build();

        Service savedService = ServiceMapper.INSTANCE.toService(serviceDto);
        when(serviceRepository.save(any(Service.class))).thenReturn(savedService);
        //Act
        servicesService.saveService(serviceDto);

        //Assert
        verify(serviceRepository,times(1)).save(any(Service.class));

    }
}