package kr.ac.nsu.hakbokgs.main.store.cart;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/BasketActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "TAG", "", "basketAdapter", "Lkr/ac/nsu/hakbokgs/main/store/cart/BasketAdapter;", "basketViewModel", "Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketViewModel;", "binding", "Lkr/ac/nsu/hakbokgs/databinding/ActivityBasketBinding;", "currentTotalAmount", "", "currentTotalPrice", "userId", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_release"})
public final class BasketActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "BasketActivity";
    private kr.ac.nsu.hakbokgs.databinding.ActivityBasketBinding binding;
    private kr.ac.nsu.hakbokgs.main.store.cart.data.BasketViewModel basketViewModel;
    private kr.ac.nsu.hakbokgs.main.store.cart.BasketAdapter basketAdapter;
    private java.lang.String userId;
    private int currentTotalPrice = 0;
    private int currentTotalAmount = 0;
    
    public BasketActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
}