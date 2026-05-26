<template>
  <view class="order-page">
    <view class="order-hero">
      <view class="order-kicker">MY ORDERS</view>
      <view class="order-title">我的订单</view>
    </view>
    <view class="tabs">
      <view class="tab" :class="{ active: activeTab === s.value }" v-for="s in statusTabs" :key="s.value" @click="switchTab(s.value)">{{ s.label }}</view>
    </view>

    <view class="order-item" v-for="order in orders" :key="order.id" @click="goDetail(order.id)">
      <view class="order-header">
        <text class="order-no">{{ order.orderNo }}</text>
        <text class="order-status" :style="{ color: statusColor(order.status) }">{{ statusText(order.status) }}</text>
      </view>
      <view class="order-goods" v-for="item in order.items" :key="item.productId">
        <image :src="item.productImage" mode="aspectFill" class="goods-img" />
        <view class="goods-info">
          <text class="goods-name">{{ item.productName }}</text>
          <text class="goods-price">¥{{ item.price }} × {{ item.quantity }}</text>
        </view>
      </view>
      <view class="order-footer">
        <text>共{{ order.items?.length || 0 }}件</text>
        <view>合计 <text class="total-price">¥{{ order.payAmount }}</text></view>
      </view>
      <view class="order-actions" v-if="order.status === 'pending'">
        <button class="btn-cancel" size="mini" @click.stop="doCancel(order.id)">取消</button>
        <button class="btn-pay" size="mini" @click.stop="doPay(order.id)">去支付</button>
      </view>
    </view>

    <view class="empty" v-if="orders.length === 0">暂无订单</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getOrderPage, cancelOrder, createPayment, mockPayOrder } from '@/api/index'

const activeTab = ref('')
const orders = ref([])
const statusTabs = [
  { label: '全部', value: '' },
  { label: '待支付', value: 'pending' },
  { label: '制作中', value: 'preparing' },
  { label: '配送中', value: 'delivering' },
  { label: '已完成', value: 'delivered' }
]

const statusMap = { pending: '待支付', paid: '已支付', preparing: '制作中', delivering: '配送中', delivered: '已送达', cancelled: '已取消' }
const colorMap = { pending: '#bd8b3b', paid: '#1f5a46', preparing: '#1f5a46', delivering: '#1f5a46', delivered: '#587763', cancelled: '#999' }
function statusText(s) { return statusMap[s] || s }
function statusColor(s) { return colorMap[s] || '#999' }

onShow(() => {
  const storedStatus = uni.getStorageSync('orderStatusFilter')
  if (storedStatus !== undefined) {
    activeTab.value = storedStatus
    uni.removeStorageSync('orderStatusFilter')
  }
  fetchOrders()
})

async function fetchOrders() {
  try {
    const data = await getOrderPage({ page: 1, pageSize: 50, status: activeTab.value || undefined })
    orders.value = data.records || []
  } catch { /* */ }
}

function switchTab(v) { activeTab.value = v; fetchOrders() }

function goDetail(id) { uni.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` }) }

async function doCancel(id) {
  try {
    await cancelOrder(id)
    uni.showToast({ title: '已取消' })
    fetchOrders()
  } catch { /* */ }
}

async function doPay(id) {
  try {
    const payData = await createPayment(id)
    if (payData.mock) {
      await mockPayOrder(id)
      uni.showToast({ title: '测试支付成功' })
      fetchOrders()
      return
    }
    uni.requestPayment({
      provider: 'wxpay',
      timeStamp: payData.timeStamp,
      nonceStr: payData.nonceStr,
      package: payData.package,
      signType: payData.signType,
      paySign: payData.paySign,
      success() { uni.showToast({ title: '支付成功' }); fetchOrders() },
      fail() { uni.showToast({ title: '支付失败', icon: 'none' }) }
    })
  } catch { /* */ }
}
</script>

<style scoped>
.order-page { min-height: 100vh; padding: 16px 0 24px; background: #f6f1e8; }
.order-hero { margin: 0 14px 12px; padding: 20px 18px; border-radius: 28px; color: #fff2d8; background: radial-gradient(circle at 92% 8%, rgba(226,192,120,.36), transparent 36%), linear-gradient(135deg, #214f40, #17372e); box-shadow: 0 16px 30px rgba(31,90,70,.2); }
.order-kicker { color: #e2c078; font-size: 10px; letter-spacing: 3px; }
.order-title { margin-top: 6px; font-size: 25px; font-weight: 900; }
.tabs { display: flex; margin: 0 14px 12px; padding: 6px; border-radius: 22px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 10px 22px rgba(83,59,33,.08); }
.tab { flex: 1; height: 36px; line-height: 36px; border-radius: 18px; text-align: center; font-size: 12px; color: #746856; }
.tab.active { color: #fff2d8; background: #1f5a46; font-weight: 800; box-shadow: 0 8px 18px rgba(31,90,70,.2); }
.order-item { background: #fffaf2; margin: 12px 14px; border-radius: 24px; overflow: hidden; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.order-header { display: flex; justify-content: space-between; padding: 14px 14px 10px; border-bottom: 1px solid #f0e5d3; }
.order-no { max-width: 230px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 12px; color: #8a7d69; }
.order-status { font-size: 13px; font-weight: 900; }
.order-goods { display: flex; padding: 10px 14px; align-items: center; }
.goods-img { width: 54px; height: 54px; border-radius: 14px; background: #eee1cf; }
.goods-info { flex: 1; min-width: 0; margin-left: 10px; }
.goods-name { color: #22352d; font-size: 14px; font-weight: 800; display: block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.goods-price { color: #9a8d78; font-size: 12px; display: block; margin-top: 4px; }
.order-footer { display: flex; justify-content: space-between; align-items: center; padding: 12px 14px; font-size: 13px; color: #746856; border-top: 1px solid #f0e5d3; }
.total-price { color: #1f5a46; font-weight: 900; font-size: 17px; }
.order-actions { display: flex; justify-content: flex-end; padding: 0 14px 14px; gap: 8px; }
.btn-cancel { background: #fffaf2; color: #786b5a; border: 1px solid #d8c8ad; border-radius: 16px; }
.btn-pay { background: #1f5a46; color: #fff2d8; border-radius: 16px; }
.empty { text-align: center; padding-top: 92px; color: #9a8d78; font-size: 14px; }
</style>
