package kr.ac.nsu.hakbokgs.main.store.order.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\fJ*\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\b0\u000fJ^\u0010\u0011\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042N\u0010\u000e\u001aJ\u0012\u001f\u0012\u001d\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00100\u0013\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0016\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u00040\u0017\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0018\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\b0\u0012J\u001a\u0010\u001a\u001a\u00020\u00102\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u0013J\u001e\u0010\u001c\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u0019R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u001e"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/order/data/UserOrderDataRepository;", "", "()V", "tag", "", "getTag", "()Ljava/lang/String;", "deleteOrderMenu", "", "userId", "orderId", "onComplete", "Lkotlin/Function0;", "getOrderDataByOrderId", "onUpdate", "Lkotlin/Function1;", "Lkr/ac/nsu/hakbokgs/main/store/order/data/OrderData;", "listenOrderChanges", "Lkotlin/Function3;", "", "Lkotlin/ParameterName;", "name", "updateData", "", "removeDataId", "", "parseOrderData", "data", "updateTotalComplete", "totalComplete", "app_debug"})
public final class UserOrderDataRepository {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String tag = "Firestore";
    @org.jetbrains.annotations.NotNull()
    public static final kr.ac.nsu.hakbokgs.main.store.order.data.UserOrderDataRepository INSTANCE = null;
    
    private UserOrderDataRepository() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTag() {
        return null;
    }
    
    public final void listenOrderChanges(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super java.util.Map<java.lang.String, kr.ac.nsu.hakbokgs.main.store.order.data.OrderData>, ? super java.util.List<java.lang.String>, ? super java.lang.Boolean, kotlin.Unit> onUpdate) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kr.ac.nsu.hakbokgs.main.store.order.data.OrderData parseOrderData(@org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.lang.Object> data) {
        return null;
    }
    
    public final void deleteOrderMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String orderId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onComplete) {
    }
    
    public final void getOrderDataByOrderId(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String orderId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kr.ac.nsu.hakbokgs.main.store.order.data.OrderData, kotlin.Unit> onUpdate) {
    }
    
    public final void updateTotalComplete(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String orderId, boolean totalComplete) {
    }
}