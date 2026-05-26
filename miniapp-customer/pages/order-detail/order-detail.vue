<template>
  <view class="order-detail" v-if="order">
    <view class="status-bar" :class="order.status">
      <view class="status-kicker">ORDER STATUS</view>
      <view class="status-text">{{ statusText(order.status) }}</view>
      <view class="status-hint" v-if="order.status === 'pending'">请尽快完成支付，甜品师等你下单</view>
      <view class="status-hint" v-if="order.status === 'delivering'">配送员正在赶来，请留意电话</view>
    </view>

    <view class="section">
      <view class="section-title">配送信息</view>
      <view class="addr-row">{{ order.deliveryContact }} {{ order.deliveryPhone }}</view>
      <view class="addr-row muted">{{ order.deliveryAddress }}</view>
    </view>

    <view class="section" v-if="order.deliveryInfo">
      <view class="section-title">配送员</view>
      <view class="info-text">{{ order.deliveryInfo.personName }} {{ order.deliveryInfo.personPhone }}</view>
    </view>

    <view class="section">
      <view class="section-title">商品明细</view>
      <view class="goods-row" v-for="item in order.items" :key="item.productId">
        <image :src="item.productImage" mode="aspectFill" class="goods-img" />
        <view class="goods-info">
          <text class="goods-name">{{ item.productName }}</text>
          <text class="goods-qty">× {{ item.quantity }}</text>
        </view>
        <text class="goods-price">¥{{ item.amount }}</text>
      </view>
    </view>

    <view class="section">
      <view class="info-row"><text>订单号</text><text>{{ order.orderNo }}</text></view>
      <view class="info-row"><text>下单时间</text><text>{{ order.createTime }}</text></view>
      <view class="info-row"><text>支付时间</text><text>{{ order.payTime || '未支付' }}</text></view>
      <view class="info-row"><text>备注</text><text>{{ order.remark || '无' }}</text></view>
    </view>

    <view class="section" v-if="order.deliveryPhoto">
      <view class="section-title">送达照片</view>
      <image :src="order.deliveryPhoto" mode="widthFix" class="delivery-photo" />
    </view>

    <view class="total-row">
      <text>合计</text><text class="total-price">¥{{ order.payAmount }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOrderDetail } from '@/api/index'

const order = ref(null)

const statusMap = { pending: '待支付', paid: '已支付', preparing: '制作中', delivering: '配送中', delivered: '已送达', cancelled: '已取消' }
function statusText(s) { return statusMap[s] || s }

onMounted(async () => {
  const pages = getCurrentPages()
  const query = pages[pages.length - 1].$page?.options || {}
  try { order.value = await getOrderDetail(query.id) } catch { /* */ }
})
</script>

<style scoped>
.order-detail { min-height: 100vh; padding: 14px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.status-bar { padding: 22px 18px; color: #fff2d8; border-radius: 28px; background: radial-gradient(circle at 92% 8%, rgba(226,192,120,.36), transparent 36%), linear-gradient(135deg, #214f40, #17372e); box-shadow: 0 16px 30px rgba(31,90,70,.2); }
.status-bar.pending { background: radial-gradient(circle at 92% 8%, rgba(255,221,152,.38), transparent 36%), linear-gradient(135deg, #785e2d, #3c3123); }
.status-bar.cancelled { background: linear-gradient(135deg, #7b756d, #4b4741); }
.status-kicker { color: #e2c078; font-size: 10px; letter-spacing: 3px; }
.status-text { margin-top: 8px; font-size: 25px; font-weight: 900; }
.status-hint { font-size: 13px; opacity: .82; margin-top: 7px; }
.section { background: #fffaf2; padding: 16px; margin-top: 12px; border-radius: 24px; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.section-title { color: #22352d; font-size: 16px; font-weight: 900; margin-bottom: 10px; }
.addr-row, .info-text { font-size: 14px; color: #22352d; line-height: 1.7; }
.muted { color: #746856; }
.goods-row { display: flex; align-items: center; padding: 8px 0; }
.goods-img { width: 48px; height: 48px; border-radius: 14px; background: #eee1cf; }
.goods-info { flex: 1; min-width: 0; margin-left: 10px; font-size: 13px; }
.goods-name { display: block; color: #22352d; font-weight: 800; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.goods-qty { color: #9a8d78; margin-top: 3px; display: block; }
.goods-price { color: #1f5a46; font-size: 14px; font-weight: 900; }
.info-row { display: flex; justify-content: space-between; gap: 12px; font-size: 13px; padding: 7px 0; color: #786b5a; }
.info-row text:last-child { color: #22352d; text-align: right; }
.delivery-photo { width: 100%; border-radius: 18px; }
.total-row { margin-top: 12px; padding: 16px; border-radius: 24px; background: #fffaf2; display: flex; justify-content: space-between; align-items: center; color: #746856; font-size: 14px; border: 1px solid #eadbc4; }
.total-price { color: #1f5a46; font-weight: 900; font-size: 24px; }
</style>
