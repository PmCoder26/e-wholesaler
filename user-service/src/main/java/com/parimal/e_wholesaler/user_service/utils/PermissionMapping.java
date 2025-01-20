package com.parimal.e_wholesaler.user_service.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static com.parimal.e_wholesaler.user_service.utils.Permission.*;
import static com.parimal.e_wholesaler.user_service.utils.UserType.*;


public class PermissionMapping {

    private static final Map<UserType, Set<Permission>> map = Map.of(
            OWNER, Set.of(
                    WORKER_CREATE, WORKER_UPDATE, WORKER_DELETE, WORKER_VIEW,
                    OWNER_VIEW, OWNER_CREATE, OWNER_UPDATE, OWNER_DELETE,
                    ORDER_VIEW, ORDER_CREATE, ORDER_UPDATE, ORDER_DELETE,
                    PRODUCT_VIEW, PRODUCT_CREATE, PRODUCT_UPDATE, PRODUCT_DELETE,
                    SALES_VIEW, SALES_CREATE, SALES_UPDATE, SALES_DELETE,
                    SHOP_VIEW, SHOP_CREATE, SHOP_UPDATE, SHOP_DELETE),
            WORKER, Set.of(
                    WORKER_VIEW,
                    ORDER_VIEW, ORDER_CREATE, ORDER_UPDATE, ORDER_DELETE,
                    PRODUCT_VIEW,
                    SALES_UPDATE,
                    SHOP_VIEW),
            CUSTOMER, Set.of(
                    CUSTOMER_VIEW,
                    CUSTOMER_CREATE,
                    CUSTOMER_UPDATE,
                    CUSTOMER_DELETE)
    );

    public static Set<SimpleGrantedAuthority> getAuthorities(UserType userType) {
        return map.get(userType)
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

}
