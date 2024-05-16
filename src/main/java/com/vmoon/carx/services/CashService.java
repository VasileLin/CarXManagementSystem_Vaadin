package com.vmoon.carx.services;

import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.dto.CashGridDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CashService {
    void saveCash(CashDto customer);

    Page<CashGridDto> allCashDate(PageRequest pageRequest, LocalDate value, LocalDate value1);

    long countDateResult(LocalDate value, LocalDate toDatePickerValue);

    CashDto getCashByTransactionNo(String transactionNo);

    Page<CashGridDto> searchCash(String value, Pageable pageRequest);

    long countSearchResults(String value);
}
