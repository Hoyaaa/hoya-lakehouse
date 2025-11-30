package kr.ac.nsu.hakbokgs.main.store.popup;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\u000e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0013J(\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u0005H\u0002J\u0016\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\u0005J\u000e\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u0005J\u0006\u0010\u001d\u001a\u00020\u0010R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\b\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\f0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/popup/MultiStoreCookingWatcher;", "", "()V", "listenerRegistrations", "", "", "Lcom/google/firebase/firestore/ListenerRegistration;", "orderNumberToDocIdMap", "pendingPopups", "", "Lkotlin/Triple;", "previousCompleteMap", "", "shownPopupStores", "", "schedulePopupRetry", "", "showPendingPopups", "activity", "Landroid/app/Activity;", "showPopupNow", "context", "store", "menu", "orderNumber", "startWatching", "userId", "orderDocId", "startWatchingAllStores", "stopWatching", "app_release"})
public final class MultiStoreCookingWatcher {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, com.google.firebase.firestore.ListenerRegistration> listenerRegistrations = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, java.lang.Boolean> previousCompleteMap = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.String> shownPopupStores = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<kotlin.Triple<java.lang.String, java.lang.String, java.lang.String>> pendingPopups = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, java.lang.String> orderNumberToDocIdMap = null;
    @org.jetbrains.annotations.NotNull()
    public static final kr.ac.nsu.hakbokgs.main.store.popup.MultiStoreCookingWatcher INSTANCE = null;
    
    private MultiStoreCookingWatcher() {
        super();
    }
    
    public final void stopWatching() {
    }
    
    public final void startWatching(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String orderDocId) {
    }
    
    public final void showPendingPopups(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
    }
    
    private final void showPopupNow(android.app.Activity context, java.lang.String store, java.lang.String menu, java.lang.String orderNumber) {
    }
    
    private final void schedulePopupRetry() {
    }
    
    public final void startWatchingAllStores(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
}