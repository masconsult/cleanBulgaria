package eu.masconsult;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

/**
 * User: Venelin Valkov <venelin@masconsult.eu>
 * Date: 08-09-2011
 */
@RunWith(RobolectricTestRunner.class)
public class HomeTest {

  @Test
  public void shouldExistActivity() {
    assertNotNull(new HomeActivity());
  }
}
