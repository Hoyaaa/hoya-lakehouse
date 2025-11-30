package kr.ac.nsu.hakbokgs.main.store.cart.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\fJ(\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0018\u0010\u000b\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f\u0012\u0004\u0012\u00020\b0\u000eJ$\u0010\u0011\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00102\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\fR\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0013"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketDataRepository;", "", "()V", "tag", "", "getTag", "()Ljava/lang/String;", "deleteBasketMenu", "", "userId", "id", "onComplete", "Lkotlin/Function0;", "getBasketMenu", "Lkotlin/Function1;", "", "Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketMenu;", "updateBasketMenu", "updateMenu", "app_debug"})
public final class BasketDataRepository {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String tag = "BasketDataResource";
    @org.jetbrains.annotations.NotNull()
    public static final kr.ac.nsu.hakbokgs.main.store.cart.data.BasketDataRepository INSTANCE = null;
    
    private BasketDataRepository() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTag() {
        return null;
    }
    
    public final void getBasketMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu>, kotlin.Unit> onComplete) {
    }
    
    public final void deleteBasketMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onComplete) {
    }
    
    public final void updateBasketMenu(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu updateMenu, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onComplete) {
    }
}