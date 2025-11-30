package kr.ac.nsu.hakbokgs.main.store.cart.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u000e\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0006J\u0016\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020\u0006J\u0006\u0010 \u001a\u00020\u001bJ(\u0010!\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0002J\u000e\u0010#\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\tJ\u0016\u0010$\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u000bJ\u001e\u0010&\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u000bJ\u0014\u0010\'\u001a\u00020\u001b2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R&\u0010\u0007\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b0\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R)\u0010\u0011\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b0\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0014\u0010\u0013\u001a\u00020\tX\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0010R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0010\u00a8\u0006)"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_basketList", "Landroidx/lifecycle/MutableLiveData;", "", "Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketMenu;", "_groupByStoreBasketMap", "", "", "_totalAmount", "", "_totalPriceAll", "basketList", "Landroidx/lifecycle/LiveData;", "getBasketList", "()Landroidx/lifecycle/LiveData;", "filteredList", "getFilteredList", "tag", "getTag", "()Ljava/lang/String;", "totalAmount", "getTotalAmount", "totalPriceAll", "getTotalPriceAll", "deleteBasketMenu", "", "menu", "deleteItemFromFirestore", "userId", "item", "getTotalPriceAndAmount", "groupByStore", "list", "loadBasketData", "updateAmount", "newAmount", "updateAmountInFirestore", "updateBasketList", "newList", "app_release"})
public final class BasketViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tag = "BasketViewModel";
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.MutableLiveData<java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu>> _basketList;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.MutableLiveData<java.util.Map<java.lang.String, java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu>>> _groupByStoreBasketMap;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Integer> _totalPriceAll = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Integer> _totalAmount = null;
    
    public BasketViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTag() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu>> getBasketList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.Map<java.lang.String, java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu>>> getFilteredList() {
        return null;
    }
    
    public final void loadBasketData(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    private final java.util.Map<java.lang.String, java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu>> groupByStore(java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu> list) {
        return null;
    }
    
    public final void updateBasketList(@org.jetbrains.annotations.NotNull()
    java.util.List<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu> newList) {
    }
    
    public final void deleteBasketMenu(@org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu menu) {
    }
    
    public final void updateAmount(@org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu menu, int newAmount) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Integer> getTotalPriceAll() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Integer> getTotalAmount() {
        return null;
    }
    
    public final void getTotalPriceAndAmount() {
    }
    
    public final void deleteItemFromFirestore(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu item) {
    }
    
    public final void updateAmountInFirestore(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu item, int newAmount) {
    }
}