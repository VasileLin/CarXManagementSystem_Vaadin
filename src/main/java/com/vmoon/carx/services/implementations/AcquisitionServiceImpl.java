package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.AcquisitionDto;
import com.vmoon.carx.mappers.AcquisitionMapper;
import com.vmoon.carx.repositories.AcquisitionRepository;
import com.vmoon.carx.services.AcquisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcquisitionServiceImpl implements AcquisitionService {

    private final AcquisitionRepository acquisitionRepository;

    @Override
    public void saveAcquisition(AcquisitionDto acquisition) {
        acquisitionRepository.save(AcquisitionMapper.toAcquisition(acquisition));
    }
}
