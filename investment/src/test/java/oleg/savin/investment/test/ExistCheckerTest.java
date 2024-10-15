//package oleg.savin.investment.test;
//
//import jakarta.persistence.EntityNotFoundException;
//import oleg.savin.investment.checker.ExistChecker;
//import oleg.savin.investment.order.OrderRepository;
//import oleg.savin.investment.order.feign.UserClient;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import static org.junit.Assert.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class ExistCheckerTest {
//
//    @MockBean
//    private UserClient userClient;
//
//    @MockBean
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private ExistChecker existChecker;
//
//    @Test
//    public void shouldThrowExceptionIfUserDoesNotExist() {
//        when(userClient.existsById(anyLong())).thenReturn(false);
//        assertThrows(EntityNotFoundException.class, () -> existChecker.isUserExist(1L));
//    }
//
//    @Test
//    public void shouldNotThrowExceptionIfUserExists() {
//        when(userClient.existsById(anyLong())).thenReturn(true);
//        assertDoesNotThrow(() -> existChecker.isUserExist(1L));
//    }
//}
