package com.vmoon.carx.services.implementations;

import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.dto.CashGridDto;
import com.vmoon.carx.entities.Cash;
import com.vmoon.carx.mappers.CashMapper;
import com.vmoon.carx.repositories.CashRepository;
import com.vmoon.carx.services.CashService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class CashServiceImpl implements CashService {

    private final CashRepository cashRepository;

    @Override
    public void saveCash(CashDto customer) {
        if (customer != null) {
            cashRepository.save(CashMapper.toCash(customer));
        }
    }

    @Override
    public Page<CashGridDto> allCashDate(PageRequest pageRequest, LocalDate fromValue, LocalDate toValue) {
        return cashRepository.findAllByDate(pageRequest, fromValue, toValue).map(CashMapper::toCashGridDto);
    }

    @Override
    public long countDateResult(LocalDate fromDatePickerValue, LocalDate toDatePickerValue) {
        return cashRepository.countAllByDate(fromDatePickerValue, toDatePickerValue);
    }

    @Override
    public CashDto getCashByTransactionNo(String transactionNo) {
        return CashMapper.toCashDto(cashRepository.getCashByTransactionNo(transactionNo));
    }

    @Override
    public Page<CashGridDto> searchCash(String value, Pageable pageable) {
        Specification<Cash> specification = textInAllColumns(value);
        Page<Cash> page = cashRepository.findAll(specification, pageable);
        return page.map(CashMapper::toCashGridDto);
    }

    private Specification<Cash> textInAllColumns(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final String finalText = "%" + text.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("transactionNo")), finalText));
    }

    @Override
    public long countSearchResults(String value) {
        return cashRepository.count(textInAllColumns(value));
    }
}
