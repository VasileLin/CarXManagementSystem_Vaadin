package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.mappers.CashMapper;
import com.vmoon.carx.repositories.CashRepository;
import com.vmoon.carx.services.CashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

    private final CashRepository cashRepository;

    @Override
    public void saveCash(CashDto customer) {
        if (customer != null) {
            cashRepository.save(CashMapper.toCash(customer));
        }
    }
}
