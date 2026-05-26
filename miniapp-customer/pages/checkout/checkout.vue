<template>
  <view class="checkout">
    <view class="page-title">确认订单</view>

    <view class="address-section" @click="goSelectAddress">
      <template v-if="selectedAddress">
        <view class="addr-kicker">配送至</view>
        <view class="addr-top">
          <text class="addr-name">{{ selectedAddress.name }}</text>
          <text class="addr-tel">{{ selectedAddress.contactPhone }}</text>
        </view>
        <view class="addr-detail">{{ selectedAddress.address }}</view>
      </template>
      <template v-else>
        <view>
          <view class="addr-kicker">配送地址</view>
          <text class="addr-empty">请选择配送地址</text>
        </view>
        <text class="arrow">›</text>
      </template>
    </view>

    <view class="goods-section">
      <view class="section-title">商品明细</view>
      <view class="goods-item" v-for="item in checkedItems" :key="item.id">
        <image :src="item.coverImage" mode="aspectFill" class="goods-img" />
        <view class="goods-info">
          <view class="goods-name">{{ item.productName }}</view>
          <view class="goods-sku" v-if="item.skuInfo">{{ item.skuInfo }}</view>
          <view class="goods-price">¥{{ item.price }} × {{ item.quantity }}</view>
        </view>
        <view class="goods-total">¥{{ (item.price * item.quantity).toFixed(2) }}</view>
      </view>
    </view>

    <view class="remark-section">
      <text>备注</text>
      <input v-model="remark" placeholder="口味、餐具等需求" placeholder-class="placeholder" class="remark-input" />
    </view>

    <view class="summary">
      <text>合计</text>
      <text class="summary-price">¥{{ totalPrice }}</text>
    </view>

    <button class="btn-pay" @click="doSubmit">提交订单</button>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useCartStore } from '@/store/cart'
import { submitOrder, createPayment, mockPayOrder } from '@/api/index'

const cartStore = useCartStore()
const selectedAddress = ref(null)
const remark = ref('')

const checkedItems = computed(() => cartStore.items.filter(i => i.checked))
const totalPrice = computed(() => {
  return checkedItems.value.reduce((s, i) => s + i.price * i.quantity, 0).toFixed(2)
})

onShow(() => {
  const pages = getCurrentPages()
  const current = pages[pages.length - 1]
  if (current?.__exposeAddr) selectedAddress.value = current.__exposeAddr
})

function goSelectAddress() {
  uni.navigateTo({ url: '/pages/address/address' })
}

async function doSubmit() {
  if (!selectedAddress.value) {
    uni.showToast({ title: '请选择配送地址', icon: 'none' })
    return
  }
  try {
    const order = await submitOrder({ addressId: selectedAddress.value.id, remark: remark.value })
    const payData = await createPayment(order.id)
    if (payData.mock) {
      await mockPayOrder(order.id)
      uni.showToast({ title: '测试支付成功' })
      uni.redirectTo({ url: '/pages/order/order' })
      return
    }
    uni.requestPayment({
      provider: 'wxpay',
      timeStamp: payData.timeStamp,
      nonceStr: payData.nonceStr,
      package: payData.package,
      signType: payData.signType,
      paySign: payData.paySign,
      success() {
        uni.showToast({ title: '支付成功' })
        uni.redirectTo({ url: '/pages/order/order' })
      },
      fail() {
        uni.showToast({ title: '支付失败', icon: 'none' })
      }
    })
  } catch { /* */ }
}
</script>

<style scoped>
.checkout { min-height: 100vh; padding: 18px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.page-title { color: #22352d; font-size: 26px; font-weight: 900; margin-bottom: 14px; }
.address-section { min-height: 86px; padding: 16px; border-radius: 24px; background: radial-gradient(circle at 92% 8%, rgba(226,192,120,.36), transparent 36%), linear-gradient(135deg, #214f40, #17372e); color: #fff2d8; box-shadow: 0 16px 30px rgba(31,90,70,.2); display: flex; justify-content: space-between; align-items: center; }
.addr-kicker { color: #e2c078; font-size: 11px; letter-spacing: 2px; margin-bottom: 7px; }
.addr-top { font-size: 16px; font-weight: 800; }
.addr-tel { margin-left: 14px; color: rgba(255,242,216,.78); }
.addr-detail { font-size: 13px; color: rgba(255,242,216,.82); margin-top: 7px; line-height: 1.6; }
.addr-empty { font-size: 17px; font-weight: 800; }
.arrow { font-size: 28px; color: rgba(255,242,216,.75); }
.goods-section, .remark-section, .summary { margin-top: 12px; padding: 16px; border-radius: 24px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.section-title { color: #22352d; font-size: 16px; font-weight: 900; margin-bottom: 12px; }
.goods-item { display: flex; padding: 10px 0; border-bottom: 1px solid #f0e5d3; align-items: center; }
.goods-item:last-child { border-bottom: 0; }
.goods-img { width: 62px; height: 62px; border-radius: 16px; background: #eee1cf; }
.goods-info { flex: 1; min-width: 0; margin-left: 10px; }
.goods-name { color: #22352d; font-size: 14px; font-weight: 800; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.goods-sku { font-size: 11px; color: #9a8d78; margin-top: 3px; }
.goods-price { font-size: 12px; color: #7c705f; margin-top: 5px; }
.goods-total { color: #1f5a46; font-size: 14px; font-weight: 900; }
.remark-section { display: flex; align-items: center; font-size: 14px; color: #22352d; font-weight: 800; }
.remark-input { flex: 1; margin-left: 14px; font-size: 14px; color: #22352d; }
.placeholder { color: #aa9f8d; }
.summary { display: flex; justify-content: space-between; align-items: center; font-size: 14px; color: #746856; }
.summary-price { color: #1f5a46; font-weight: 900; font-size: 24px; }
.btn-pay { margin-top: 22px; background: linear-gradient(135deg, #23634c, #153c31); color: #fff2d8; border-radius: 26px; font-size: 16px; height: 52px; line-height: 52px; box-shadow: 0 14px 24px rgba(31,90,70,.24); }
</style>
