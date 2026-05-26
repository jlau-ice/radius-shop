<template>
  <view class="records-page">
    <view class="page-title">配送记录</view>
    <view class="order-card" v-for="order in orders" :key="order.id" @click="goDetail(order.id)">
      <view class="order-header">
        <text class="order-no">{{ order.orderNo }}</text>
        <text class="order-time">{{ order.deliveryTime || order.createTime }}</text>
      </view>
      <view class="addr-row">{{ order.deliveryAddress }}</view>
      <view class="contact-row">{{ order.deliveryContact }} {{ order.deliveryPhone }}</view>
      <view class="goods-summary">
        <text v-for="(item, i) in order.items" :key="i">{{ item.productName }}×{{ item.quantity }}{{ i < order.items.length - 1 ? '、' : '' }}</text>
      </view>
      <view class="amount-row">¥{{ order.payAmount }}</view>
    </view>
    <view class="empty" v-if="orders.length === 0 && !loading">暂无配送记录</view>
    <view class="loading" v-if="loading">加载中...</view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDoneOrders } from '@/api/index'

const orders = ref([])
const loading = ref(false)

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try {
    const data = await getDoneOrders()
    orders.value = data?.records || []
  } catch { /* */ }
  finally { loading.value = false }
}

function goDetail(id) {
  uni.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` })
}
</script>

<style scoped>
.records-page { min-height: 100vh; padding: 18px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.page-title { color: #22352d; font-size: 25px; font-weight: 900; margin-bottom: 14px; }
.order-card { background: #fffaf2; margin-bottom: 12px; padding: 14px; border-radius: 24px; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.order-header { display: flex; justify-content: space-between; gap: 10px; font-size: 11px; color: #9a8d78; }
.order-no { flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.addr-row { color: #22352d; font-size: 15px; font-weight: 800; margin-top: 10px; line-height: 1.45; }
.contact-row { color: #746856; font-size: 13px; margin-top: 6px; }
.goods-summary { color: #9a8d78; font-size: 12px; margin-top: 8px; }
.amount-row { text-align: right; color: #1f5a46; font-weight: 900; font-size: 16px; margin-top: 10px; padding-top: 10px; border-top: 1px solid #f0e5d3; }
.empty, .loading { text-align: center; padding-top: 90px; color: #9a8d78; font-size: 14px; }
.loading { padding-top: 20px; }
</style>
