package kr.ac.nsu.hakbokgs.main.store.hamburger.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001BA\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\bJ\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003JE\u0010\u001a\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u00d6\u0003J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001J\t\u0010!\u001a\u00020\"H\u00d6\u0001R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\n\"\u0004\b\u000e\u0010\fR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\n\"\u0004\b\u0010\u0010\fR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\n\"\u0004\b\u0012\u0010\fR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\n\"\u0004\b\u0014\u0010\f\u00a8\u0006#"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/Size;", "Ljava/io/Serializable;", "basics", "Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/SizePrice;", "single", "set", "medium", "small", "(Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/SizePrice;Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/SizePrice;Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/SizePrice;Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/SizePrice;Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/SizePrice;)V", "getBasics", "()Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/SizePrice;", "setBasics", "(Lkr/ac/nsu/hakbokgs/main/store/hamburger/db/SizePrice;)V", "getMedium", "setMedium", "getSet", "setSet", "getSingle", "setSingle", "getSmall", "setSmall", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
public final class Size implements java.io.Serializable {
    @org.jetbrains.annotations.Nullable()
    private kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice basics;
    @org.jetbrains.annotations.Nullable()
    private kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice single;
    @org.jetbrains.annotations.Nullable()
    private kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice set;
    @org.jetbrains.annotations.Nullable()
    private kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice medium;
    @org.jetbrains.annotations.Nullable()
    private kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice small;
    
    public Size(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice basics, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice single, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice set, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice medium, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice small) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice getBasics() {
        return null;
    }
    
    public final void setBasics(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice getSingle() {
        return null;
    }
    
    public final void setSingle(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice getSet() {
        return null;
    }
    
    public final void setSet(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice getMedium() {
        return null;
    }
    
    public final void setMedium(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice getSmall() {
        return null;
    }
    
    public final void setSmall(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice p0) {
    }
    
    public Size() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kr.ac.nsu.hakbokgs.main.store.hamburger.db.Size copy(@org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice basics, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice single, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice set, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice medium, @org.jetbrains.annotations.Nullable()
    kr.ac.nsu.hakbokgs.main.store.hamburger.db.SizePrice small) {
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