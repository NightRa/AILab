import scala.Function1;
import scala.math.Ordering;

import java.util.Arrays;

public class JavaUtil {
    public static <A, B> void sortBy(A[] arr, Function1<A,B> f, Ordering<B> ordering) {
        Arrays.sort(arr, (x, y) -> ordering.compare(f.apply(x), f.apply(y)));
    }
}
