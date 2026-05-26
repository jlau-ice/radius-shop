<template>
  <view class="pending-page">
    <view class="hero">
      <view class="hero-kicker">TODAY</view>
      <view class="hero-title">配送工作台</view>
      <view class="hero-subtitle">先接单，再完成配送签收</view>
    </view>

    <view class="tabs">
      <view class="tab" :class="{ active: activeTab === 'pending' }" @click="switchTab('pending')">待接单</view>
      <view class="tab" :class="{ active: activeTab === 'delivering' }" @click="switchTab('delivering')">配送中</view>
    </view>

    <view class="order-card" v-for="order in orders" :key="order.id" @click="goDetail(order.id)">
      <view class="order-header">
        <text class="order-no">{{ order.orderNo }}</text>
        <text class="order-time">{{ order.createTime }}</text>
      </view>
      <view class="addr-row">{{ order.deliveryAddress }}</view>
      <view class="contact-row">{{ order.deliveryContact }} {{ order.deliveryPhone }}</view>
      <view class="goods-summary">
        <text v-for="(item, i) in order.items" :key="i">{{ item.productName }}×{{ item.quantity }}{{ i < order.items.length - 1 ? '、' : '' }}</text>
      </view>
      <view class="order-footer">
        <text class="amount">¥{{ order.payAmount }}</text>
        <button v-if="activeTab === 'pending'" class="btn-accept" size="mini" @click.stop="doAccept(order.id)">接单</button>
        <button v-else class="btn-accept" size="mini" @click.stop="goComplete(order.id)">完成配送</button>
      </view>
    </view>
    <view class="empty" v-if="orders.length === 0 && !loading">{{ emptyText }}</view>
    <view class="loading" v-if="loading">加载中...</view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getPendingOrders, getDeliveringOrders, acceptOrder } from '@/api/index'

const activeTab = ref('pending')
const orders = ref([])
const loading = ref(false)
const emptyText = computed(() => activeTab.value === 'pending' ? '暂无待接单订单' : '暂无配送中订单')

onShow(() => fetchData())

async function fetchData() {
  loading.value = true
  try {
    const data = activeTab.value === 'pending' ? await getPendingOrders() : await getDeliveringOrders()
    orders.value = data?.records || []
  } catch { /* */ }
  finally { loading.value = false }
}

function switchTab(tab) {
  if (activeTab.value === tab) return
  activeTab.value = tab
  fetchData()
}

async function doAccept(id) {
  try {
    await acceptOrder(id)
    uni.showToast({ title: '接单成功' })
    activeTab.value = 'delivering'
    fetchData()
  } catch { /* */ }
}

function goDetail(id) {
  uni.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` })
}

function goComplete(id) {
  uni.navigateTo({ url: `/pages/complete/complete?id=${id}` })
}
</script>

<style scoped>
.pending-page { min-height: 100vh; padding: 16px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.hero { padding: 20px 18px; margin-bottom: 12px; border-radius: 28px; color: #fff2d8; background: radial-gradient(circle at 90% 8%, rgba(226,192,120,.36), transparent 35%), linear-gradient(135deg, #214f40, #17372e); box-shadow: 0 16px 30px rgba(31,90,70,.2); }
.hero-kicker { color: #e2c078; font-size: 10px; letter-spacing: 3px; }
.hero-title { margin-top: 6px; font-size: 25px; font-weight: 900; }
.hero-subtitle { margin-top: 6px; color: rgba(255,242,216,.75); font-size: 13px; }
.tabs { display: flex; margin-bottom: 12px; padding: 6px; border-radius: 22px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 10px 22px rgba(83,59,33,.08); }
.tab { flex: 1; height: 36px; line-height: 36px; border-radius: 18px; text-align: center; font-size: 13px; color: #746856; }
.tab.active { color: #fff2d8; background: #1f5a46; font-weight: 900; box-shadow: 0 8px 18px rgba(31,90,70,.2); }
.order-card { background: #fffaf2; margin-bottom: 12px; padding: 14px; border-radius: 24px; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.order-header { display: flex; justify-content: space-between; gap: 10px; font-size: 11px; color: #9a8d78; }
.order-no { flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.addr-row { color: #22352d; font-size: 16px; font-weight: 900; margin-top: 10px; line-height: 1.45; }
.contact-row { color: #746856; font-size: 13px; margin-top: 6px; }
.goods-summary { color: #9a8d78; font-size: 12px; margin-top: 8px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.order-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 12px; padding-top: 12px; border-top: 1px solid #f0e5d3; }
.amount { color: #1f5a46; font-weight: 900; font-size: 18px; }
.btn-accept { background: #1f5a46; color: #fff2d8; border-radius: 16px; padding: 6px 18px; }
.empty, .loading { text-align: center; padding-top: 90px; color: #9a8d78; font-size: 14px; }
.loading { padding-top: 20px; }
</style>
