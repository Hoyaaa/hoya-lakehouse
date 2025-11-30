package kr.ac.nsu.hakbokgs.main.store.cart.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001:\u0001\u0007B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\b"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/data/OrderNumManage;", "", "()V", "getOrderNum", "", "listener", "Lkr/ac/nsu/hakbokgs/main/store/cart/data/OrderNumManage$OnOrderNumListener;", "OnOrderNumListener", "app_release"})
public final class OrderNumManage {
    @org.jetbrains.annotations.NotNull()
    public static final kr.ac.nsu.hakbokgs.main.store.cart.data.OrderNumManage INSTANCE = null;
    
    private OrderNumManage() {
        super();
    }
    
    public final void getOrderNum(@org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.cart.data.OrderNumManage.OnOrderNumListener listener) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u000e\u0010\u0004\u001a\n\u0018\u00010\u0005j\u0004\u0018\u0001`\u0006H&J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH&\u00a8\u0006\n"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/data/OrderNumManage$OnOrderNumListener;", "", "onFailure", "", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "newNum", "", "app_release"})
    public static abstract interface OnOrderNumListener {
        
        public abstract void onSuccess(int newNum);
        
        public abstract void onFailure(@org.jetbrains.annotations.Nullable()
        java.lang.Exception e);
    }
}