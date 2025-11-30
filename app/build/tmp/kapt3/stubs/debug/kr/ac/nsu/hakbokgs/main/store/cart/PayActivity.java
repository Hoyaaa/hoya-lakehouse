package kr.ac.nsu.hakbokgs.main.store.cart;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0014J4\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000f0\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0004H\u0002J4\u0010\u0016\u001a\u00020\n2\u001a\u0010\u0017\u001a\u0016\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u0018j\n\u0012\u0004\u0012\u00020\u0012\u0018\u0001`\u00192\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u0004H\u0002J,\u0010\u001b\u001a\u00020\n2\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u00042\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000f0\u000eH\u0002J\u001c\u0010\u001e\u001a\u00020\n2\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000f0\u000eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/PayActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "TAG", "", "radioGroup", "Landroid/widget/RadioGroup;", "selectedPayment", "userId", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "regroupOrderData", "", "", "orderList", "", "Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketMenu;", "orderNum", "", "payment", "saveOrderToFireStore", "basketList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "orderId", "updateOrderDataInFirestore", "docId", "groupByStoreMap", "updateStoreDataInFirestore", "data", "app_debug"})
public final class PayActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "PayActivity";
    private android.widget.RadioGroup radioGroup;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String selectedPayment = "";
    private java.lang.String userId;
    
    public PayActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void saveOrderToFireStore(java.util.ArrayList<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu> basketList, java.lang.String userId, java.lang.String orderId) {
    }
    
    private final java.util.Map<java.lang.String, java.lang.Object> regroupOrderData(java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu> orderList, int orderNum, java.lang.String payment) {
        return null;
    }
    
    private final void updateOrderDataInFirestore(java.lang.String userId, java.lang.String docId, java.util.Map<java.lang.String, ? extends java.lang.Object> groupByStoreMap) {
    }
    
    private final void updateStoreDataInFirestore(java.util.Map<java.lang.String, ? extends java.lang.Object> data) {
    }
}