package kr.ac.nsu.hakbokgs.main.store.order.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B=\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u000fJ\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u0015\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0003JH\u0010\u0019\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0001\u00a2\u0006\u0002\u0010\u001aJ\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u001f\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006 "}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/order/data/OrderData;", "", "orderNum", "", "payment", "", "orderDate", "Lcom/google/firebase/Timestamp;", "storeMap", "", "Lkr/ac/nsu/hakbokgs/main/store/order/data/StoreOrder;", "(Ljava/lang/Integer;Ljava/lang/String;Lcom/google/firebase/Timestamp;Ljava/util/Map;)V", "getOrderDate", "()Lcom/google/firebase/Timestamp;", "getOrderNum", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getPayment", "()Ljava/lang/String;", "getStoreMap", "()Ljava/util/Map;", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/Integer;Ljava/lang/String;Lcom/google/firebase/Timestamp;Ljava/util/Map;)Lkr/ac/nsu/hakbokgs/main/store/order/data/OrderData;", "equals", "", "other", "hashCode", "toString", "app_release"})
public final class OrderData {
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer orderNum = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String payment = null;
    @org.jetbrains.annotations.Nullable()
    private final com.google.firebase.Timestamp orderDate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, kr.ac.nsu.hakbokgs.main.store.order.data.StoreOrder> storeMap = null;
    
    public OrderData(@org.jetbrains.annotations.Nullable()
    java.lang.Integer orderNum, @org.jetbrains.annotations.Nullable()
    java.lang.String payment, @org.jetbrains.annotations.Nullable()
    com.google.firebase.Timestamp orderDate, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, kr.ac.nsu.hakbokgs.main.store.order.data.StoreOrder> storeMap) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getOrderNum() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPayment() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.firebase.Timestamp getOrderDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, kr.ac.nsu.hakbokgs.main.store.order.data.StoreOrder> getStoreMap() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.firebase.Timestamp component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, kr.ac.nsu.hakbokgs.main.store.order.data.StoreOrder> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kr.ac.nsu.hakbokgs.main.store.order.data.OrderData copy(@org.jetbrains.annotations.Nullable()
    java.lang.Integer orderNum, @org.jetbrains.annotations.Nullable()
    java.lang.String payment, @org.jetbrains.annotations.Nullable()
    com.google.firebase.Timestamp orderDate, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, kr.ac.nsu.hakbokgs.main.store.order.data.StoreOrder> storeMap) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}