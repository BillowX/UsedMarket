package com.maker.use.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMConversation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.adapter.LCIMCommonListAdapter;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.leancloud.chatkit.event.LCIMOfflineMessageCountChangeEvent;
import cn.leancloud.chatkit.view.LCIMDividerItemDecoration;
import cn.leancloud.chatkit.viewholder.LCIMConversationItemHolder;
import de.greenrobot.event.EventBus;

/**
 * Created by XT on 2016/9/28.
 */

public class MessageFragment extends BaseFragment {
    protected SwipeRefreshLayout refreshLayout;
    protected RecyclerView recyclerView;
    protected LCIMCommonListAdapter<AVIMConversation> itemAdapter;
    protected LinearLayoutManager layoutManager;

    public MessageFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(cn.leancloud.chatkit.R.layout.lcim_conversation_list_fragment, container, false);

        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(cn.leancloud.chatkit.R.id.fragment_conversation_srl_pullrefresh);
        this.recyclerView = (RecyclerView) view.findViewById(cn.leancloud.chatkit.R.id.fragment_conversation_srl_view);
        this.refreshLayout.setEnabled(false);
        this.layoutManager = new LinearLayoutManager(this.getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.addItemDecoration(new LCIMDividerItemDecoration(this.getActivity()));
        this.itemAdapter = new LCIMCommonListAdapter(LCIMConversationItemHolder.class);
        this.recyclerView.setAdapter(this.itemAdapter);
        EventBus.getDefault().register(this);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.updateConversationList();
    }

    public void onResume() {
        super.onResume();
        this.updateConversationList();
    }

    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void updateConversationList() {
        List convIdList = LCIMConversationItemCache.getInstance().getSortedConversationList();
        ArrayList conversationList = new ArrayList();
        Iterator i$ = convIdList.iterator();

        while (i$.hasNext()) {
            String convId = (String) i$.next();
            conversationList.add(LCChatKit.getInstance().getClient().getConversation(convId));
        }

        this.itemAdapter.setDataList(conversationList);
        this.itemAdapter.notifyDataSetChanged();
    }
    /**
     * 收到对方消息时响应此事件
     *
     * @param event
     */
    public void onEvent(LCIMIMTypeMessageEvent event) {
        updateConversationList();
    }

    /**
     * 删除会话列表中的某个 item
     * @param event
     */
    public void onEvent(LCIMConversationItemLongClickEvent event) {
        if (null != event.conversation) {
            String conversationId = event.conversation.getConversationId();
            LCIMConversationItemCache.getInstance().deleteConversation(conversationId);
            updateConversationList();
        }
    }
    /**
     * 离线消息数量发生变化是响应此事件
     * 避免登陆后先进入此页面，然后才收到离线消息数量的通知导致的页面不刷新的问题
     * @param updateEvent
     */
    public void onEvent(LCIMOfflineMessageCountChangeEvent updateEvent) {
        updateConversationList();
    }

    class LCIMConversationItemLongClickEvent {
        public AVIMConversation conversation;

        public LCIMConversationItemLongClickEvent(AVIMConversation conversation) {
            this.conversation = conversation;
        }
    }
}
