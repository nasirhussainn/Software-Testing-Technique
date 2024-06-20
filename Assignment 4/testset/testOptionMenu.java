import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;

public class testOptionMenu {
    private OptionMenu optionMenu;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        optionMenu = new OptionMenu();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        optionMenu.data.put(123456, new Account(123456, 7890, 2000, 2000));
    }

    private void setInput(String input) {
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);
        optionMenu.menuInput = new Scanner(System.in);
    }

    @Test
    public void testGetLogin_Success() throws IOException {
        // Tests: JSD, IOD, COR
        setInput("123456\n7890\n3\n");
        optionMenu.getLogin();
        String output = outContent.toString();
        assertTrue(output.contains("Select the account you want to access:"));
    }

    @Test
    public void testGetLogin_Failure() throws IOException {
        // Tests: ROR, COR
        setInput("123456\n0000\n123456\n7890\n3\n");
        optionMenu.getLogin();
        String output = outContent.toString();
        assertTrue(output.contains("Wrong Customer Number or Pin Number"));
    }

    @Test
    public void testCreateAccount_Success() throws IOException {
        // Tests: IOD, ROR
        setInput("999999\n1111\n");
        optionMenu.createAccount();
        assertTrue(optionMenu.data.containsKey(999999));
        assertEquals(1111, optionMenu.data.get(999999).getPinNumber());
    }

    @Test
    public void testGetAccountType_Checking() throws IOException {
        // Tests: COR, JSD
        setInput("1\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        String output = outContent.toString();
        assertTrue(output.contains("Checkings Account:"));
    }

    @Test
    public void testGetAccountType_Savings() throws IOException {
        // Tests: COR, JSD
        setInput("2\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        String output = outContent.toString();
        assertTrue(output.contains("Savings Account:"));
    }

    @Test
    public void testMainMenu_Login() throws IOException {
        // Tests: JSD, COR
        setInput("1\n123456\n7890\n3\n");
        optionMenu.mainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("Select the account you want to access:"));
    }

    @Test
    public void testMainMenu_CreateAccount() throws IOException {
        // Tests: IOD, ROR
        setInput("2\n999999\n1111\n");
        optionMenu.mainMenu();
        assertTrue(optionMenu.data.containsKey(999999));
        assertEquals(1111, optionMenu.data.get(999999).getPinNumber());
    }

    @Test
    public void testGetChecking_ViewBalance() throws IOException {
        // Test for checking account view balance functionality
        setInput("1\n1\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        String output = outContent.toString();
        assertTrue(output.contains("Checkings Account Balance: $2,000.00"));
    }

    @Test
    public void testGetChecking_Withdraw() throws IOException {
        // Test for checking account withdraw functionality
        setInput("1\n2\n500\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        assertEquals(1500.0, acc.getCheckingBalance(), 0.001);
    }

    @Test
    public void testGetChecking_Deposit() throws IOException {
        // Test for checking account deposit functionality
        setInput("1\n3\n500\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        assertEquals(2500.0, acc.getCheckingBalance(), 0.001);
    }

    @Test
    public void testGetChecking_Transfer() throws IOException {
        // Test for checking account transfer functionality
        setInput("1\n4\n500\n1\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        assertEquals(1500.0, acc.getCheckingBalance(), 0.001);
        assertEquals(2500.0, acc.getSavingBalance(), 0.001);
    }

    @Test
    public void testGetSaving_ViewBalance() throws IOException {
        // Test for savings account view balance functionality
        setInput("2\n1\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        String output = outContent.toString();
        assertTrue(output.contains("Savings Account Balance: $2,000.00"));
    }

    @Test
    public void testGetSaving_Withdraw() throws IOException {
        // Test for savings account withdraw functionality
        setInput("2\n2\n500\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        assertEquals(1500.0, acc.getSavingBalance(), 0.001);
    }

    @Test
    public void testGetSaving_Deposit() throws IOException {
        // Test for savings account deposit functionality
        setInput("2\n3\n500\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        assertEquals(2500.0, acc.getSavingBalance(), 0.001);
    }

    @Test
    public void testGetSaving_Transfer() throws IOException {
        // Test for savings account transfer functionality
        setInput("2\n4\n500\n1\n5\n");
        Account acc = optionMenu.data.get(123456);
        optionMenu.getAccountType(acc);
        assertEquals(2500.0, acc.getCheckingBalance(), 0.001);
        assertEquals(1500.0, acc.getSavingBalance(), 0.001);
    }
}
