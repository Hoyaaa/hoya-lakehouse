package kr.ac.nsu.hakbokgs.main.store.cart;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 \u00162\u0012\u0012\u0004\u0012\u00020\u0002\u0012\b\u0012\u00060\u0003R\u00020\u00000\u0001:\u0003\u0014\u0015\u0016B\u001d\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\u00020\f2\n\u0010\r\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u001c\u0010\u0010\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000fH\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/BasketStoreAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketMenu;", "Lkr/ac/nsu/hakbokgs/main/store/cart/BasketStoreAdapter$BasketStoreViewHolder;", "userId", "", "context", "Landroid/content/Context;", "basketViewModel", "Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketViewModel;", "(Ljava/lang/String;Landroid/content/Context;Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketViewModel;)V", "onBindViewHolder", "", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "BasketMenuDiffCallback", "BasketStoreViewHolder", "Companion", "app_debug"})
public final class BasketStoreAdapter extends androidx.recyclerview.widget.ListAdapter<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu, kr.ac.nsu.hakbokgs.main.store.cart.BasketStoreAdapter.BasketStoreViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String userId = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kr.ac.nsu.hakbokgs.main.store.cart.data.BasketViewModel basketViewModel = null;
    private static int totalPriceAll = 0;
    @org.jetbrains.annotations.NotNull()
    public static final kr.ac.nsu.hakbokgs.main.store.cart.BasketStoreAdapter.Companion Companion = null;
    
    public BasketStoreAdapter(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.cart.data.BasketViewModel basketViewModel) {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kr.ac.nsu.hakbokgs.main.store.cart.BasketStoreAdapter.BasketStoreViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    kr.ac.nsu.hakbokgs.main.store.cart.BasketStoreAdapter.BasketStoreViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/BasketStoreAdapter$BasketMenuDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lkr/ac/nsu/hakbokgs/main/store/cart/data/BasketMenu;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    public static final class BasketMenuDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu> {
        
        public BasketMenuDiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu oldItem, @org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu oldItem, @org.jetbrains.annotations.NotNull()
        kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\bR\u0011\u0010\u000f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\bR\u0011\u0010\u0011\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\fR\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\bR\u0011\u0010\u0019\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\fR\u0011\u0010\u001b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\b\u00a8\u0006\u001d"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/BasketStoreAdapter$BasketStoreViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lkr/ac/nsu/hakbokgs/main/store/cart/BasketStoreAdapter;Landroid/view/View;)V", "amount", "Landroid/widget/TextView;", "getAmount", "()Landroid/widget/TextView;", "deleteButton", "Landroid/widget/ImageView;", "getDeleteButton", "()Landroid/widget/ImageView;", "menu", "getMenu", "menuPrice", "getMenuPrice", "minusButton", "getMinusButton", "optionContainer", "Landroid/widget/LinearLayout;", "getOptionContainer", "()Landroid/widget/LinearLayout;", "optionText", "getOptionText", "plusButton", "getPlusButton", "totalPrice", "getTotalPrice", "app_debug"})
    public final class BasketStoreViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView menu = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView menuPrice = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView totalPrice = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView amount = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.LinearLayout optionContainer = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView deleteButton = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView plusButton = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.ImageView minusButton = null;
        @org.jetbrains.annotations.NotNull()
        private final android.widget.TextView optionText = null;
        
        public BasketStoreViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getMenu() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getMenuPrice() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTotalPrice() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getAmount() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.LinearLayout getOptionContainer() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getDeleteButton() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getPlusButton() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.ImageView getMinusButton() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getOptionText() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/cart/BasketStoreAdapter$Companion;", "", "()V", "totalPriceAll", "", "getTotalPriceAll", "()I", "setTotalPriceAll", "(I)V", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final int getTotalPriceAll() {
            return 0;
        }
        
        public final void setTotalPriceAll(int p0) {
        }
    }
}