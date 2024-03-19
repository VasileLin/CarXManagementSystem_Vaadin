package com.vmoon.carx.mappers;

import com.vmoon.carx.dto.CashDto;
import com.vmoon.carx.entities.Cash;
import com.vmoon.carx.entities.Customer;
import com.vmoon.carx.entities.Goods;
import com.vmoon.carx.entities.Service;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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
            cashDto.setServices(cash.getServices().stream().map(ServiceMapper::toServiceDto).toList());
        }

        if (cash.getGoods() != null) {
            cashDto.setGoods(cash.getGoods().stream().map(GoodsMapper::toGoodsDto).toList());
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

        List<Service> services = cashDto.getServices() != null
                ? cashDto.getServices().stream().map(ServiceMapper::toService).collect(Collectors.toList())
                : Collections.emptyList();
        cash.setServices(services);

        List<Goods> goods = cashDto.getGoods() != null
                ? cashDto.getGoods().stream().map(GoodsMapper::toGoods).collect(Collectors.toList())
                : Collections.emptyList();
        cash.setGoods(goods);

        Customer customer = CustomerMapper.mapToCustomer(cashDto.getCustomer());
        cash.setCustomer(customer);

        return cash;
    }
}
