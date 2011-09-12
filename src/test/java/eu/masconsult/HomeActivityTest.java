package eu.masconsult;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import eu.masconsult.activity.HomeActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

/**
 * User: Venelin Valkov <venelin@masconsult.eu>
 * Date: 09-09-2011
 */
@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

  @Test
  public void shouldHaveActivity() {
    assertNotNull(new HomeActivity());
  }
}
