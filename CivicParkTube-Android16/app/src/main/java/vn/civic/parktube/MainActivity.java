package vn.civic.parktube;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.graphics.Insets;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class MainActivity extends Activity {
    private static final String HOME = "https://m.youtube.com/";
    private FrameLayout root;
    private LinearLayout page;
    private WebView web;
    private EditText query;
    private View fullscreen;
    private WebChromeClient.CustomViewCallback fullscreenCb;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(20, 21, 27));
        getWindow().setNavigationBarColor(Color.rgb(20, 21, 27));
        root = new FrameLayout(this);
        root.setBackgroundColor(Color.rgb(20, 21, 27));
        if (Build.VERSION.SDK_INT >= 30) {
            root.setOnApplyWindowInsetsListener((v, w) -> {
                Insets i = w.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                v.setPadding(i.left, i.top, i.right, i.bottom);
                return w;
            });
        }
        setContentView(root);

        page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        root.addView(page, new FrameLayout.LayoutParams(-1, -1));

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(5), dp(4), dp(5), dp(4));
        toolbar.setBackgroundColor(Color.rgb(34, 37, 45));
        page.addView(toolbar, new LinearLayout.LayoutParams(-1, dp(58)));
        addButton(toolbar, "⌂", dp(44), () -> web.loadUrl(HOME));
        addButton(toolbar, "‹", dp(42), () -> {
            if (web.canGoBack()) web.goBack(); else web.loadUrl(HOME);
        });

        query = new EditText(this);
        query.setSingleLine(true);
        query.setTextSize(14);
        query.setTextColor(Color.WHITE);
        query.setHintTextColor(Color.LTGRAY);
        query.setHint("Tìm video hoặc dán link YouTube");
        query.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        LinearLayout.LayoutParams qp = new LinearLayout.LayoutParams(0, -1, 1f);
        toolbar.addView(query, qp);
        query.setOnEditorActionListener((v, id, event) -> { search(); return true; });
        addButton(toolbar, "MỞ", dp(50), this::search);
        addButton(toolbar, "↗", dp(45), this::openExternal);

        TextView notice = new TextView(this);
        notice.setText("Chỉ xem video khi xe đã đỗ. Android Auto có thể không cho phép ứng dụng video.");
        notice.setGravity(Gravity.CENTER);
        notice.setTextSize(11);
        notice.setTextColor(Color.LTGRAY);
        notice.setBackgroundColor(Color.rgb(28, 30, 38));
        notice.setPadding(dp(5), dp(4), dp(5), dp(4));
        page.addView(notice, new LinearLayout.LayoutParams(-1, -2));

        web = new WebView(this);
        web.setBackgroundColor(Color.BLACK);
        WebSettings cfg = web.getSettings();
        cfg.setJavaScriptEnabled(true);
        cfg.setDomStorageEnabled(true);
        cfg.setMediaPlaybackRequiresUserGesture(true);
        cfg.setAllowFileAccess(false);
        cfg.setAllowContentAccess(false);
        cfg.setSupportMultipleWindows(false);
        if (Build.VERSION.SDK_INT >= 26) cfg.setSafeBrowsingEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);

        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, android.webkit.WebResourceRequest req) {
                Uri uri = req.getUrl();
                String host = uri.getHost();
                if ("https".equalsIgnoreCase(uri.getScheme()) && allowed(host)) return false;
                return true;
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override public void onShowCustomView(View view, CustomViewCallback cb) {
                if (fullscreen != null) { cb.onCustomViewHidden(); return; }
                fullscreen = view; fullscreenCb = cb;
                page.setVisibility(View.GONE);
                root.addView(fullscreen, new FrameLayout.LayoutParams(-1,-1));
            }
            @Override public void onHideCustomView() { hideFullscreen(); }
        });
        page.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));
        web.loadUrl(HOME);
    }
    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density + 0.5f); }
    private void addButton(LinearLayout row, String name, int w, Runnable callback) {
        Button b = new Button(this);
        b.setText(name); b.setAllCaps(false); b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(200, 41, 48)));
        row.addView(b, new LinearLayout.LayoutParams(w, dp(48)));
        b.setOnClickListener(v -> callback.run());
    }
    private boolean allowed(String host) {
        if (host == null) return false;
        String h = host.toLowerCase(Locale.ROOT);
        return h.equals("youtube.com") || h.endsWith(".youtube.com") ||
               h.equals("youtu.be") || h.equals("youtube-nocookie.com") || h.endsWith(".youtube-nocookie.com");
    }
    private String toUrl(String input) {
        String s = input.trim();
        if (s.length() == 0) return HOME;
        if (s.startsWith("https://") || s.startsWith("http://")) {
            Uri u = Uri.parse(s);
            return allowed(u.getHost()) ? u.buildUpon().scheme("https").build().toString() : null;
        }
        if (s.matches("(?i)(www\\.|m\\.)?(youtube\\.com|youtu\\.be)/.*")) return "https://" + s;
        return "https://m.youtube.com/results?search_query=" + URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
    private void search() {
        String url = toUrl(query.getText().toString());
        if (url == null) { Toast.makeText(this, "Chỉ hỗ trợ YouTube", Toast.LENGTH_SHORT).show(); return; }
        web.loadUrl(url);
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(query.getWindowToken(), 0);
        query.clearFocus();
    }
    private void openExternal() {
        String s = web.getUrl();
        if (s == null || !s.startsWith("https://")) s = HOME;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        try { startActivity(intent); } catch (Exception e) {
            Toast.makeText(this, "Không có trình duyệt phù hợp", Toast.LENGTH_SHORT).show();
        }
    }
    private void hideFullscreen() {
        if (fullscreen == null) return;
        root.removeView(fullscreen);
        fullscreen = null;
        if (fullscreenCb != null) { fullscreenCb.onCustomViewHidden(); fullscreenCb = null; }
        page.setVisibility(View.VISIBLE);
    }
    @Override public void onBackPressed() {
        if (fullscreen != null) hideFullscreen();
        else if (web != null && web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }
    @Override protected void onPause() {
        if (web != null) web.onPause();
        super.onPause();
    }
    @Override protected void onResume() {
        super.onResume();
        if (web != null) web.onResume();
    }
    @Override protected void onDestroy() {
        hideFullscreen();
        if (web != null) { web.stopLoading(); web.destroy(); web = null; }
        super.onDestroy();
    }
}
