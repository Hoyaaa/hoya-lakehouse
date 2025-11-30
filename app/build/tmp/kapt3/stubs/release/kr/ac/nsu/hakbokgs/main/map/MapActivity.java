package kr.ac.nsu.hakbokgs.main.map;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0012\u0010\u0012\u001a\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\u0010\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\fH\u0002J\u001e\u0010\u0017\u001a\u00020\n2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00192\u0006\u0010\u000b\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lkr/ac/nsu/hakbokgs/main/map/MapActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "mapLayer", "Landroid/widget/FrameLayout;", "movingImageView", "Landroid/widget/ImageView;", "routeLineView", "Lkr/ac/nsu/hakbokgs/main/map/RouteLineView;", "drawPathToBuilding", "", "buildingId", "", "getCenterPoint", "Landroid/graphics/PointF;", "view", "Landroid/view/View;", "getPopupLayout", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "showPopup", "layoutId", "startDrawingRouteAndWalking", "routePoints", "", "app_release"})
public final class MapActivity extends androidx.appcompat.app.AppCompatActivity {
    private android.widget.FrameLayout mapLayer;
    private kr.ac.nsu.hakbokgs.main.map.RouteLineView routeLineView;
    private android.widget.ImageView movingImageView;
    
    public MapActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void drawPathToBuilding(int buildingId) {
    }
    
    private final void startDrawingRouteAndWalking(java.util.List<? extends android.graphics.PointF> routePoints, int buildingId) {
    }
    
    private final android.graphics.PointF getCenterPoint(android.view.View view) {
        return null;
    }
    
    private final int getPopupLayout(int buildingId) {
        return 0;
    }
    
    private final void showPopup(int layoutId) {
    }
}