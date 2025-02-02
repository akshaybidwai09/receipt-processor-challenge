
import org.example.model.Item;
import org.example.model.Receipt;
import org.example.receiptUtil.ReceiptUtils;
import org.example.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ReceiptServiceTest {


    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        receiptService = new ReceiptService();
    }

    @Test
    void testValidReceiptProcessing() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("35.35");
        receipt.setItems(List.of(
                new Item("Mountain Dew 12PK", "6.49"),
                new Item("Emils Cheese Pizza", "12.25"),
                new Item("Knorr Creamy Chicken", "1.26"),
                new Item("Doritos Nacho Cheese", "3.35"),
                new Item("Klarbrunn 12-PK 12 FL OZ", "12.00")
        ));

        String receiptId = receiptService.processReceipt(receipt);
        assertNotNull(receiptId);
        assertEquals(28, receiptService.getPoints(receiptId));
    }

    @Test
    void testAnotherValidReceipt() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("M&M Corner Market");
        receipt.setPurchaseDate("2022-03-20");
        receipt.setPurchaseTime("14:33");
        receipt.setTotal("9.00");
        receipt.setItems(List.of(
                new Item("Pepsi", "2.25"),
                new Item("Pepsi", "2.25"),
                new Item("Pepsi", "2.25"),
                new Item("Pepsi", "2.25")
        ));

        String receiptId = receiptService.processReceipt(receipt);
        assertNotNull(receiptId);
        assertEquals(109, receiptService.getPoints(receiptId));
    }

    @Test
    void testInvalidReceiptProcessing_NullRetailer() {
        Receipt receipt = new Receipt();
        receipt.setRetailer(null);
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("35.35");
        receipt.setItems(List.of(new Item("Mountain Dew 12PK", "6.49")));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> receiptService.processReceipt(receipt));
        assertEquals("Retailer name cannot be blank.", exception.getMessage());
    }

    @Test
    void testInvalidReceiptProcessing_EmptyTotal() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Walmart");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("-1"); // negative total
        receipt.setItems(List.of(new Item("Mountain Dew 12PK", "6.49")));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> receiptService.processReceipt(receipt));
        assertEquals("Total price cannot be negative.", exception.getMessage());
    }

    @Test
    void testInvalidReceiptProcessing_NoItems() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Costco");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("10.00");
        receipt.setItems(List.of()); // No items

        Exception exception = assertThrows(IllegalArgumentException.class, () -> receiptService.processReceipt(receipt));
        assertEquals("Items list cannot be empty.", exception.getMessage());
    }

    @Test
    void testPointCalculation_RoundDollarTotal() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Costco");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("10.00"); // Round dollar total
        receipt.setItems(List.of(new Item("Product A", "5.00")));

        int points = ReceiptUtils.calculatePoints(receipt);
        assertTrue(points >= 50, "Should earn 50 points for round dollar total");
    }

    @Test
    void testPointCalculation_MultipleOfQuarter() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("25.25"); // Multiple of 0.25
        receipt.setItems(List.of(new Item("Product B", "10.00")));

        int points = ReceiptUtils.calculatePoints(receipt);
        assertTrue(points >= 25, "Should earn 25 points for total being a multiple of 0.25");
    }

    @Test
    void testPointCalculation_OddDayPurchase() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Amazon");
        receipt.setPurchaseDate("2022-01-03"); // Odd day
        receipt.setPurchaseTime("13:01");
        receipt.setTotal("20.00");
        receipt.setItems(List.of(new Item("Product C", "10.00")));

        int points = ReceiptUtils.calculatePoints(receipt);
        assertTrue(points >= 6, "Should earn 6 points for purchasing on an odd day");
    }

    @Test
    void testPointCalculation_Between2PMand4PM() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Amazon");
        receipt.setPurchaseDate("2022-01-02");
        receipt.setPurchaseTime("14:30");
        receipt.setTotal("20.00");
        receipt.setItems(List.of(new Item("Product C", "10.00")));

        int points = ReceiptUtils.calculatePoints(receipt);
        assertTrue(points >= 10, "Should earn 10 points for purchasing between 2:00 PM and 4:00 PM");
    }

    @Test
    void testInvalidReceiptID_ReturnsNull() {
        assertNull(receiptService.getPoints("invalid-id"), "Should return null for non-existent receipt ID");
    }
}