package com.vmoon.carx.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionNoGeneratorTest {
    @Test
    public void testTransactionNoGeneratorPositive() {
        String transactionNo = Generators.transactionNoGenerator();
        assertNotNull(transactionNo);
        assertTrue(transactionNo.matches("\\d{8}\\d{5}")); // Check if the generated transaction number format is correct
    }

    @Test
    public void testTransactionNoGeneratorNegative() {
        String transactionNo = Generators.transactionNoGenerator();
        assertNotNull(transactionNo);
        assertFalse(transactionNo.isEmpty()); // Check that the generated transaction number is not empty
        assertFalse(transactionNo.contains("-")); // Check that the generated transaction number does not contain "-"
        assertFalse(transactionNo.contains(".")); // Check that the generated transaction number does not contain "."
    }
}
