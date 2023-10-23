package com.thereto.theretosvirtuales.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.thereto.theretosvirtuales.databinding.FragmentHomeBinding;

public class InicioFragment extends Fragment {

    private FragmentHomeBinding binding;
    private View mCustomView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.webView.setWebChromeClient(new WebChromeClient(){


            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                mCustomView = view;
                binding.webView.setVisibility(View.GONE);
                binding.frameLayout.setVisibility(View.VISIBLE);

                binding.frameLayout.addView(view);
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                if (mCustomView == null)
                    return;

                binding.webView.setVisibility(View.VISIBLE);
                binding.frameLayout.setVisibility(View.GONE);
                mCustomView.setVisibility(View.GONE);
                binding.frameLayout.removeView(mCustomView);

                mCustomView = null;

            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}