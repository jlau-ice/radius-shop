<template>
  <view class="detail-page" v-if="order">
    <view class="hero">
      <view class="hero-kicker">ORDER DETAIL</view>
      <view class="hero-title">订单详情</view>
      <view class="hero-subtitle">核对收货信息后再配送</view>
    </view>

    <view class="section highlight">
      <view class="section-title">收货信息</view>
      <view class="info-row"><text>收货人</text><text>{{ order.deliveryContact }}</text></view>
      <view class="info-row"><text>电话</text><text class="tel">{{ order.deliveryPhone }}</text></view>
      <view class="address">{{ order.deliveryAddress }}</view>
    </view>

    <view class="section">
      <view class="section-title">商品明细</view>
      <view class="goods-row" v-for="item in order.items" :key="item.productId">
        <text>{{ item.productName }}</text>
        <text>x{{ item.quantity }}</text>
      </view>
    </view>

    <view class="section" v-if="order.remark">
      <view class="section-title">备注</view>
      <text class="remark">{{ order.remark }}</text>
    </view>

    <view class="actions" v-if="order.status === 'delivering'">
      <button class="btn-complete" @click="goComplete">拍照签收</button>
    </view>

    <view class="actions" v-else-if="order.status === 'paid' || order.status === 'preparing'">
      <button class="btn-complete" @click="doAccept">接单配送</button>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { acceptOrder, getOrderDetail } from '@/api/index'

const order = ref(null)

onMounted(async () => {
  const pages = getCurrentPages()
  const query = pages[pages.length - 1].$page?.options || {}
  try { order.value = await getOrderDetail(query.id) } catch { /* */ }
})

async function doAccept() {
  try {
    await acceptOrder(order.value.id)
    uni.showToast({ title: '接单成功' })
    order.value.status = 'delivering'
  } catch { /* */ }
}

function goComplete() {
  uni.navigateTo({ url: `/pages/complete/complete?id=${order.value.id}` })
}
</script>

<style scoped>
.detail-page { min-height: 100vh; padding: 16px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.hero { padding: 20px 18px; border-radius: 28px; color: #fff2d8; background: radial-gradient(circle at 90% 8%, rgba(226,192,120,.36), transparent 35%), linear-gradient(135deg, #214f40, #17372e); box-shadow: 0 16px 30px rgba(31,90,70,.2); }
.hero-kicker { color: #e2c078; font-size: 10px; letter-spacing: 3px; }
.hero-title { margin-top: 6px; font-size: 25px; font-weight: 900; }
.hero-subtitle { margin-top: 6px; color: rgba(255,242,216,.75); font-size: 13px; }
.section { background: #fffaf2; padding: 16px; margin-top: 12px; border-radius: 24px; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.highlight { border-color: rgba(31,90,70,.22); }
.section-title { color: #22352d; font-size: 16px; font-weight: 900; margin-bottom: 10px; }
.info-row { display: flex; justify-content: space-between; font-size: 13px; padding: 5px 0; color: #746856; }
.info-row text:last-child { color: #22352d; font-weight: 800; }
.tel { color: #1f5a46 !important; }
.address { margin-top: 10px; padding: 12px; border-radius: 18px; background: #f4ead9; color: #22352d; font-size: 15px; font-weight: 800; line-height: 1.5; }
.goods-row { display: flex; justify-content: space-between; padding: 8px 0; color: #746856; font-size: 13px; border-bottom: 1px solid #f0e5d3; }
.goods-row:last-child { border-bottom: 0; }
.remark { color: #746856; font-size: 13px; line-height: 1.6; }
.actions { padding-top: 18px; }
.btn-complete { background: linear-gradient(135deg, #23634c, #153c31); color: #fff2d8; border-radius: 26px; height: 50px; line-height: 50px; font-size: 16px; box-shadow: 0 14px 24px rgba(31,90,70,.24); }
</style>
