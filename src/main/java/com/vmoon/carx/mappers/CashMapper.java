package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.dto.CashGridDto;
import com.vmoon.carx.entities.Cash;
import com.vmoon.carx.entities.Customer;
import com.vmoon.carx.entities.Goods;
import com.vmoon.carx.entities.Service;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CashMapper {

    public static CashDto toCashDto(Cash cash) {

        CashDto cashDto = CashDto.builder()
                .id(cash.getId())
                .transactionNo(cash.getTransactionNo())
                .price(cash.getPrice())
                .date(cash.getDate())
                .status(cash.getStatus())
                .details(cash.getDetails())
                .build();

        if (cash.getServices() != null) {
            cashDto.setServices(cash.getServices().stream().map(ServiceMapper::toServiceDto).collect(Collectors.toSet()));
        }

        if (cash.getGoods() != null) {
            cashDto.setGoods(cash.getGoods().stream().map(GoodsMapper::toGoodsDto).collect(Collectors.toSet()));
        }

        if (cash.getCustomer() != null) {
            cashDto.setCustomer( CustomerMapper.mapToCustomerDto(cash.getCustomer()));
        }

        return cashDto;
    }

    public static Cash toCash(CashDto cashDto) {
        Cash cash = Cash.builder()
                .id(cashDto.getId())
                .transactionNo(cashDto.getTransactionNo())
                .price(cashDto.getPrice())
                .date(cashDto.getDate())
                .status(cashDto.getStatus())
                .details(cashDto.getDetails())
                .build();

        Set<Service> services = cashDto.getServices() != null
                ? cashDto.getServices().stream().map(ServiceMapper::toService).collect(Collectors.toSet())
                : Collections.emptySet();
        cash.setServices(services);

        Set<Goods> goods = cashDto.getGoods() != null
                ? cashDto.getGoods().stream().map(GoodsMapper::toGoods).collect(Collectors.toSet())
                : Collections.emptySet();
        cash.setGoods(goods);

        Customer customer = CustomerMapper.mapToCustomer(cashDto.getCustomer());
        cash.setCustomer(customer);

        return cash;
    }

    public static CashGridDto toCashGridDto(Cash cashDto) {
        return CashGridDto.builder()
                .id(cashDto.getId())
                .transactionNo(cashDto.getTransactionNo())
                .price(cashDto.getPrice())
                .date(cashDto.getDate())
                .status(cashDto.getStatus())
                .details(cashDto.getDetails())
                .build();
    }

}
