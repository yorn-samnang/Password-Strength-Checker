//package tests;
//
//import org.junit.jupiter.api.Test;
//import service.PasswordChecker;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class PasswordCheckerTest {
//    PasswordChecker checker = new PasswordChecker();
//
//    @Test
//    public void testCommonPassword() {
//        assertTrue(checker.isCommonPassword("password"));
//        assertFalse(checker.isCommonPassword("Unique123!"));
//    }
//
//    @Test
//    public void testStrength() {
//        assertEquals("Weak", checker.checkStrength("12345", "user"));
//        assertEquals("Medium", checker.checkStrength("MyPass123", "user"));
//        assertEquals("Strong", checker.checkStrength("MyPass123@", "user"));
//        assertEquals("Weak", checker.checkStrength("user2025", "user"));
//        assertEquals("Weak", checker.checkStrength("aaaaaa", "user"));
//    }
//}
