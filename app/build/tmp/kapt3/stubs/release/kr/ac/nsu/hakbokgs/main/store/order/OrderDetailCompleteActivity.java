package kr.ac.nsu.hakbokgs.main.store.order;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/order/OrderDetailCompleteActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "mainRecyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "orderDetailAdapter", "Lkr/ac/nsu/hakbokgs/main/store/order/OrderDetailAdapter;", "orderViewModel", "Lkr/ac/nsu/hakbokgs/main/store/order/data/OrderViewModel;", "tag", "", "userId", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_release"})
public final class OrderDetailCompleteActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tag = "OrderDetailCompleteActivity";
    private kr.ac.nsu.hakbokgs.main.store.order.data.OrderViewModel orderViewModel;
    private androidx.recyclerview.widget.RecyclerView mainRecyclerView;
    private kr.ac.nsu.hakbokgs.main.store.order.OrderDetailAdapter orderDetailAdapter;
    private java.lang.String userId;
    
    public OrderDetailCompleteActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
}