package telrun.shortnik.config.filters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class SessionFilter{

    //тут может быть логика для зашиты от атак

}
