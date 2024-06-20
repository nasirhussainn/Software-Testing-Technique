import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class testAccount {
    private Account account;

    @Before
    public void setUp() throws Exception {
        account = new Account(12345, 6789, 1000.0, 500.0);
    }

    @After
    public void tearDown() throws Exception {
        account = null;
    }

    @Test
    public void testConstructor() {
        assertEquals(12345, account.getCustomerNumber());
        assertEquals(6789, account.getPinNumber());
        assertEquals(1000.0, account.getCheckingBalance(), 0.01);
        assertEquals(500.0, account.getSavingBalance(), 0.01);
    }

    @Test
    public void testCalcCheckingWithdraw_ValidAmount() {
        account.calcCheckingWithdraw(200);
        assertEquals(800.0, account.getCheckingBalance(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcCheckingWithdraw_InvalidAmount() {
        account.calcCheckingWithdraw(1200);
    }

    @Test
    public void testCalcSavingWithdraw_ValidAmount() {
        account.calcSavingWithdraw(200);
        assertEquals(300.0, account.getSavingBalance(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcSavingWithdraw_InvalidAmount() {
        account.calcSavingWithdraw(700);
    }

    @Test
    public void testCalcCheckingDeposit_ValidAmount() {
        account.calcCheckingDeposit(200);
        assertEquals(1200.0, account.getCheckingBalance(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcCheckingDeposit_InvalidAmount() {
        account.calcCheckingDeposit(-200);
    }

    @Test
    public void testCalcSavingDeposit_ValidAmount() {
        account.calcSavingDeposit(200);
        assertEquals(700.0, account.getSavingBalance(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcSavingDeposit_InvalidAmount() {
        account.calcSavingDeposit(-200);
    }

    @Test
    public void testCalcCheckTransfer_ValidAmount() {
        account.calcCheckTransfer(200);
        assertEquals(800.0, account.getCheckingBalance(), 0.01);
        assertEquals(700.0, account.getSavingBalance(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcCheckTransfer_InvalidAmount() {
        account.calcCheckTransfer(1200);
    }

    @Test
    public void testCalcSavingTransfer_ValidAmount() {
        account.calcSavingTransfer(200);
        assertEquals(1200.0, account.getCheckingBalance(), 0.01);
        assertEquals(300.0, account.getSavingBalance(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalcSavingTransfer_InvalidAmount() {
        account.calcSavingTransfer(700);
    }

    @Test
    public void testGetCheckingWithdrawInput_ValidAmount() {

        String input = "200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getCheckingWithdrawInput();
        assertEquals(800.0, account.getCheckingBalance(), 0.01);
    }

    @Test
    public void testGetCheckingWithdrawInput_InvalidAmount() {

        String input = "1200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getCheckingWithdrawInput();
        assertEquals(1000.0, account.getCheckingBalance(), 0.01);
    }

    @Test
    public void testGetSavingWithdrawInput_ValidAmount() {

        String input = "200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getsavingWithdrawInput();
        assertEquals(300.0, account.getSavingBalance(), 0.01);
    }

    @Test
    public void testGetSavingWithdrawInput_InvalidAmount() {

        String input = "700\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getsavingWithdrawInput();
        assertEquals(500.0, account.getSavingBalance(), 0.01);
    }

    @Test
    public void testGetCheckingDepositInput_ValidAmount() {

        String input = "200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getCheckingDepositInput();
        assertEquals(1200.0, account.getCheckingBalance(), 0.01);
    }

    @Test
    public void testGetCheckingDepositInput_InvalidAmount() {

        String input = "-200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getCheckingDepositInput();
        assertEquals(1000.0, account.getCheckingBalance(), 0.01);
    }

    @Test
    public void testGetSavingDepositInput_ValidAmount() {

        String input = "200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getSavingDepositInput();
        assertEquals(700.0, account.getSavingBalance(), 0.01);
    }

    @Test
    public void testGetSavingDepositInput_InvalidAmount() {

        String input = "-200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getSavingDepositInput();
        assertEquals(500.0, account.getSavingBalance(), 0.01);
    }

    @Test
    public void testGetTransferInput_CheckingsToSavings_ValidAmount() {

        String input = "1\n200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getTransferInput("Checkings");
        assertEquals(800.0, account.getCheckingBalance(), 0.01);
        assertEquals(700.0, account.getSavingBalance(), 0.01);
    }

    @Test
    public void testGetTransferInput_CheckingsToSavings_InvalidAmount() {

        String input = "1\n1200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getTransferInput("Checkings");
        assertEquals(1000.0, account.getCheckingBalance(), 0.01);
        assertEquals(500.0, account.getSavingBalance(), 0.01);
    }

    @Test
    public void testGetTransferInput_SavingsToCheckings_ValidAmount() {

        String input = "1\n200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getTransferInput("Savings");
        assertEquals(1200.0, account.getCheckingBalance(), 0.01);
        assertEquals(300.0, account.getSavingBalance(), 0.01);
    }

    @Test
    public void testGetTransferInput_SavingsToCheckings_InvalidAmount() {

        String input = "1\n700\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        account.getTransferInput("Savings");
        assertEquals(1000.0, account.getCheckingBalance(), 0.01);
        assertEquals(500.0, account.getSavingBalance(), 0.01);
    }
}
